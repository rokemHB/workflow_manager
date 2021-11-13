package de.unibremen.swp2.kcb.util;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.UserService;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.List;

/**
 * The class Email util.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@ManagedBean
@ApplicationScoped
public class EmailUtil {

    /**
     * Injected instnce of userService
     */
    @Inject
    private UserService userService;

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(EmailUtil.class);

    /**
     * Mail session to be used for sending emails
     */
    @Resource(lookup = "java:jboss/mail/Default")
    private Session session;

    /**
     * Email address that mails are send from
     */
    @Getter
    private String senderEmail;

    /**
     * Encoding of EmailUtil
     */
    private String encoding = "utf-8";

    /**
     * Init method for EmailUtil
     */
    @PostConstruct
    public void init() {
        final String address = System.getenv("KCB_SENDER_EMAIL");
        if (address == null || address.equals("")) {
            logger.warn("No sender email address specified in system environment. Will use default.");
            this.senderEmail = "kcb@example.com";
        } else this.senderEmail = address;
    }

    /**
     * Send email with given mimeMessage
     *
     * @param message the message to be send
     */
    public void send(final MimeMessage message) throws MessagingException {
        Transport.send(message);
    }

    /**
     * Create new Mime Message with configured mail session
     *
     * @return MimeMessage with configured session
     */
    public MimeMessage createMessage() {
        return new MimeMessage(this.session);
    }

    /**
     * Send a reset password message to the given user.
     *
     * @param resetURL generated URL of User to reset password
     * @param user     to reset password of
     */
    public void sendResetMessage(final String resetURL, final User user) throws MessagingException {
        try {
            final MimeMessage message = this.createMessage();
            final MimeMultipart multipart = new MimeMultipart();
            final MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(generateResetText(user, resetURL), encoding);

            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(generateResetHTML(user, resetURL), "text/html; charset=utf-8");

            multipart.addBodyPart(textPart);
            multipart.addBodyPart(htmlPart);

            final Address userEmailAddress = new InternetAddress(user.getEmail());

            message.setContent(multipart);
            message.setFrom(this.senderEmail);
            message.setSubject("Passwort zurücksetzen", encoding);
            message.setRecipient(Message.RecipientType.TO, userEmailAddress);

            this.send(message);

        } catch (MessagingException e) {
            logger.warn("E-Mail to user: " + user.getEmail() + " couldn't be send.");
            throw new MessagingException(e.getMessage());
        }
    }

    /**
     * Send a message in case an assembly is lost.
     *
     * @param assembly the assembly
     * @throws MessagingException the messaging exception
     */
    public void sendAssemblyLostMessage(final Assembly assembly) throws MessagingException {
        try {
            // Needed if the executing User is an Admin himself, so he does not get 2 Mails
            List<User> alreadySentTo = new ArrayList<>();

            final MimeMessage message = this.createMessage();
            final MimeMultipart multipart = new MimeMultipart();
            final MimeBodyPart textPart = new MimeBodyPart();

            textPart.setText("Assembly " + assembly.getAssemblyID() + " has been marked as lost.", encoding);

            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent("Assembly " + assembly.getAssemblyID() + " has been marked as lost.", "text/html; charset=utf-8");

            multipart.addBodyPart(textPart);
            multipart.addBodyPart(htmlPart);

            for(User u : userService.getByRole(Role.LOGISTIKER)) {
                final Address userEmailAddress = new InternetAddress(u.getEmail());

                message.setContent(multipart);
                message.setFrom(this.senderEmail);
                message.setSubject("Assembly " + assembly.getAssemblyID() + " lost", encoding);
                message.setRecipient(Message.RecipientType.TO, userEmailAddress);
                this.send(message);

                alreadySentTo.add(u);
            }

            // If the executing User is not an Admin, send him a mail as well
            if(!(alreadySentTo.contains(userService.getExecutingUser()))) {
                final Address userEmailAddress = new InternetAddress(userService.getExecutingUser().getEmail());

                message.setContent(multipart);
                message.setFrom(this.senderEmail);
                message.setSubject("Assembly " + assembly.getAssemblyID() + " lost", encoding);
                message.setRecipient(Message.RecipientType.TO, userEmailAddress);
                this.send(message);
            }



        } catch (MessagingException e) {
            logger.warn("E-Mail to user couldn't be send.");
            throw new MessagingException(e.getMessage());
        }
    }

    /**
     * generates the reset HTML for a given user
     * @param user the user
     * @param resetURL the reset URL
     * @return the string
     */
    private String generateResetHTML(User user, String resetURL) {
        return "<h2>Passwort zurücksetzen für Benutzer " + user.getUsername() + "</h2>" +
                "<p>Über folgenden Link können Sie Ihr Passwort zurücksetzen: <a href='" + resetURL + "'>" +
                "Passwort zurücksetzen</p>";
    }

    /**
     * generates the reset test for a given user
     * @param user the user
     * @param resetURL the resetURL
     * @return the string
     */
    private String generateResetText(final User user, final String resetURL) {
        return "Passwort zurücksetzen für Benutzer " + user.getUsername() +
                "\nÜber folgenden Link können Sie Ihr Passwort zurücksetzen: " + resetURL;
    }
}
