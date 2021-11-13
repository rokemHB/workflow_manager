package de.unibremen.swp2.kcb.controller;

import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.PasswordResetException;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller class to set the locale language.
 *
 * @author Marius
 */
@Named
@RequestScoped
public class PasswordResetController extends Controller {

    /**
     * Logger object of the LocaleController class
     */
    private static final Logger logger = LogManager.getLogger(PasswordResetController.class);

    /**
     * Injected instance of userService
     */
    @Inject
    private UserService userService;

    /**
     * Email attribute of PasswordResetController
     */
    @Getter
    @Setter
    private String email = "";

    /**
     * Submit the password reset.
     */
    public void submit() {
        try {
            userService.resetPassword(this.email);
            this.redirect("../login.xhtml");
        } catch (PasswordResetException e) {
            logger.debug("Password reset failed for email: " + email);
        }
    }

    /**
     * Redirect the current user to a password reset page.
     */
    public void redirectToReset() {
        try {
            final String token = userService.generateResetToken(userService.getExecutingUser());
            final String redirectUrl = userService.generateResetURL(token);
            super.redirectToPath(redirectUrl);
        } catch (PasswordResetException e) {
            logger.debug(e);
            super.displayMessageFromResource("error.summary.reset-failed", "error.detail.reset-failed", FacesMessage.SEVERITY_ERROR);
        }
    }
}
