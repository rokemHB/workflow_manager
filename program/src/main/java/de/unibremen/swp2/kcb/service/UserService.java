package de.unibremen.swp2.kcb.service;

import com.google.common.hash.Hashing;
import com.lambdaworks.crypto.SCryptUtil;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.ResetToken;
import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.ResetTokenRepository;
import de.unibremen.swp2.kcb.persistence.UserRepository;
import de.unibremen.swp2.kcb.persistence.locations.TransportRepository;
import de.unibremen.swp2.kcb.security.KCBRealm;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.util.EmailUtil;
import de.unibremen.swp2.kcb.util.EntityManagerProducer;
import de.unibremen.swp2.kcb.validator.backend.UserValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

/**
 * Service class to handle Users.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@Transactional
@ApplicationScoped
@KCBSecure
public class UserService implements Service<User> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(UserService.class);

    /**
     * Default value of scrypt r used in the application. (memory factor)
     */
    public static final int DEFAULT_SCRYPT_R = 8;

    /**
     * Default value of scrypt n used in the application. (work factor)
     */
    public static final int DEFAULT_SCRYPT_N = 16384;

    /**
     * Default value of scrypt p used in the application. (parallelization factor)
     */
    public static final int DEFAULT_SCRYPT_P = 1;

    /**
     * Injected instance of UserValidator
     */
    @Inject
    private UserValidator userValidator;

    /**
     * Injected instance of UserRepository
     */
    @Inject
    private UserRepository userRepository;

    /**
     * Injected instance of ResetTokenRepository
     */
    @Inject
    private ResetTokenRepository resetTokenRepository;

    /**
     * Injected instance of TransportRepository
     */
    @Inject
    private TransportRepository transportRepository;

    /**
     * Injected instance of WorkstationService
     */
    @Inject
    private WorkstationService workstationService;

    /**
     * Injected instance of EntityManagerProducer
     */
    @Inject
    private EntityManagerProducer entityManagerProducer;

    /**
     * Injected instance of EmailUtil
     */
    @Inject
    private EmailUtil emailUtil;

    /**
     * EntityManager instance of UserService class
     */
    private EntityManager em;


    /**
     * Setup method for UserService.
     */
    @PostConstruct
    public void setUp() {
        this.em = entityManagerProducer.getEntityManager();
    }

    /**
     * TearDown method for UserService class.
     */
    @PreDestroy
    public void tearDown() {
        this.em.close();
    }

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return User that has been created and stored
     * @throws CreationException if creation fails
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    @RequiresAuthentication
    public User create(final User entity) throws CreationException, EntityAlreadyExistingException {

        //Validating user
        boolean valid;
        try {
            logger.trace("Attempting to validate user \"{}\" ...", entity);
            valid = userValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during user validation. Can't create user.");
            throw new CreationException("Can't create user: " + e.getMessage());
        }
        logger.trace("Validation of user \"{}\" completed without exceptions.", entity);

        //Checking, if user is valid
        if (!valid) {
            final String message = "User \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("User \"{}\" is valid.", entity);

        //Hashing password
        final String hashedPassword = hashPassword(entity.getPassword());
        entity.setPassword(hashedPassword);

        //Checking, if User is already stored in database
        final List<User> storedUser = userRepository.findByUsername(entity.getUsername());
        if (!storedUser.isEmpty()) {
            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create workstation: Entity is already stored in datasource");
        }

        //Saving user
        User repoEntity;
        try {
            logger.trace("Attempting to save user \"{}\" ...", entity);
            repoEntity = userRepository.save(entity);
        } catch (PersistenceException e) {
            final String message = "Entity \"" + entity + "\" couldn't be persisted.";
            logger.debug(message);
            throw new CreationException(message);
        }

        logger.trace("Saving of user \"{}\" completed without exceptions.", entity);
        logger.info("Create user \"{}\" - triggered by: {}", entity.getUsername(), getExecutingUser().getUsername());
        logger.trace("Returning user \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Calls this.updateWithoutPermission and does the permission checks for update method of userService.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @throws UpdateException if update fails
     * @see de.unibremen.swp2.kcb.validator.backend
     */
    @Override
    @RequiresAuthentication
    public User update(final User entity) throws UpdateException {
        return this.updateWithoutPermission(entity);
    }

    /**
     * Updates the stored entity with the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @throws UpdateException if update fails
     * @see de.unibremen.swp2.kcb.validator.backend
     */
    public User updateWithoutPermission(final User entity) throws UpdateException {

        //Validating user
        boolean valid;
        try {
            logger.trace("Attempting to validate user \"{}\" ...", entity);
            valid = userValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating user \"{}\". Can't update user", entity);
            throw new UpdateException("Can't update user: " + e.getMessage());
        }
        logger.trace("Validation of user \"{}\" completed without exceptions.", entity);

        //Checking, if user is valid
        if (!valid) {
            final String message = "User \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("User \"{}\" is valid.", entity);

        //Checking, if user is stored in database
        final User storedUser = userRepository.findBy(entity.getId());
        if (storedUser == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving user
        User repoEntity;
        try {
            logger.trace("Attempting to save new user \"{}\" ...", entity);
            repoEntity = userRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting user \"{}\". Can't update user.", entity);
            throw new UpdateException("Can't update user: " + e.getMessage());
        }

        logger.trace("Saving of user \"{}\" completed without exceptions.",entity);
        // When resetting password the executing user is null since there is no seccion
        final String updatingUser = getExecutingUser() != null ? getExecutingUser().getUsername()
          : "password reset of " + entity.getUsername();
        logger.info("Update user \"{} - triggered by: {}", entity.getUsername(), updatingUser);
        logger.trace("Returning user \"{}\"", entity);
        // Clear realm cache to update user roles immediately
        this.clearRealmCache(entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException if deletion of the user fails
     */
    @Override
    @RequiresAuthentication
    public void delete(final User entity) throws DeletionException {

        final User executingUser = this.getExecutingUser();

        // Can't delete the executing user
        if (executingUser.equals(entity)) {
            logger.debug("Tried to delete executing user. Will abort deletion.");
            throw new SelfDestructionException("Can't delete executing user");
        }

        //Checking, if user is stored in database
        final User storedUser = userRepository.findBy(entity.getId());
        if (storedUser == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Detach Technologe from workstations
        if (entity.getRoles().contains(Role.TECHNOLOGE)) {
            List <Workstation> workstations;
            try {
                workstations = workstationService.getByUser(entity);
            } catch (FindByException e) {
                throw new DeletionException("Can't delete user: " + e.getMessage());
            }

            for (Workstation workstation : workstations) {
                workstation.getUsers().remove(entity);
                logger.debug("Removed user from workstation {}.", workstation.getName());
            }
        }

        //Deleting user
        try {
            logger.trace("Attempting to delete user \"{}\" ...", entity);
            userRepository.attachAndRemove(entity);
            logger.info("Delete user \"{}\" triggered by: {}",entity.getUsername(), getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting user \"{}\". Can't delete user.", entity);
            throw new DeletionException("Can't delete user: " + e.getMessage());
        }
    }

    /**
     * Get the currently executing user. Can be used to get information about the executing user in every context.
     *
     * @return currently executing user
     */
    @RequiresAuthentication
    public User getExecutingUser() {
        final Subject executingSubject = SecurityUtils.getSubject();

        if (executingSubject == null)
            return null;

        final List<User> executingUsers = userRepository.findByUsername((String) executingSubject.getPrincipal());

        if (executingUsers == null || executingUsers.size() < 1)
            return null;

        // user that executes the operation
        return executingUsers.get(0);
    }

    /**
     * Get the currently executing subject. Can be used to get information about the executing subject in every context.
     *
     * @return currently executing subject
     */
    @RequiresAuthentication
    public Subject getExecutingSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * Return the {@link User} with the given name.
     *
     * @param username of the user
     * @return {@link User} with the given name
     */
    public User getByUsername(final String username) {
        if (username == null)
            return null;
        final List<User> users = userRepository.findByUsername(username);
        if (users == null || users.size() == 0) {
            logger.debug("No users found for username {}.", username);
            return null;
        }
        if (users.size() > 1)
            logger.warn("Found {} users with username = {}. Will use first match.", users.size(), username);
        return users.get(0);
    }

    /**
     * Return the {@link User} with the given email.
     *
     * @param email of the email
     * @return {@link User} with the given email address
     */
    public User getByEmail(final String email) {
        if (email == null)
            return null;
        final List<User> users = userRepository.findByEmail(email);
        if (users == null || users.size() == 0) {
            logger.debug("No users found for email {}", email);
            return null;
        }
        if (users.size() > 1)
            logger.warn("Found {} users with email = {}. Will use first match.",
              users.size(), email);
        return users.get(0);
    }

    /**
     * Return all Users with the given role.
     *
     * @param role to get users for
     * @return List of all users with the given role
     */
    @RequiresAuthentication
    public List<User> getByRole(final Role role) {
        final List<User> allUsers = this.getAll();
        final List<User> result = new ArrayList<>();
        for (User u : allUsers) {
            if (u.getRoles().contains(role)) result.add(u);
        }
        return result;
    }

    /**
     * Return all existing Assemblies.
     *
     * @return Collection of all existing Assemblies.
     */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Send an email to a group of users.
     *
     * @param users   Collection of {@link User} to send the email to
     * @param message to be send to the users
     * @see MimeMessage
     */
    public void notify(final Collection<User> users, final MimeMessage message) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Send an E-Mail to a newly registered Users E-Mail Adress
     * @param user to be notified
     */
    public void sendNewAccountMail(User user) throws MessagingException {
        try {
            final String newAccountText = "Hello " + user.getFirstName() + " " + user.getLastName() + ", you registered an Account with the Username " + user.getUsername() + ".";
            final MimeMessage message = emailUtil.createMessage();
            final MimeMultipart multipart = new MimeMultipart();
            final MimeBodyPart textPart = new MimeBodyPart();

            textPart.setText(newAccountText, "utf-8");

            multipart.addBodyPart(textPart);

            final Address userEmailAddress = new InternetAddress(user.getEmail());

            message.setContent(multipart);
            message.setFrom(emailUtil.getSenderEmail());
            message.setSubject("New Account registered", "utf-8");
            message.setRecipient(Message.RecipientType.TO, userEmailAddress);

            emailUtil.send(message);
        } catch (MessagingException e) {
            logger.warn("E-Mail to user: {} couldn't be send.", user.getEmail());
            throw new MessagingException(e.getMessage());
        }
    }

    /**
     * Return Collection of {@link User} with a given first name.
     *
     * @param firstName The first name.
     * @return Collection of {@link User} with a given first name.
     */
    public List<User> getByFirstName(String firstName) {
        if (firstName == null)
            return null;
        return userRepository.findByFirstName(firstName);
    }

    /**
     * Return Collection of {@link User} with a given last name.
     *
     * @param lastName The last name.
     * @return Collection of {@link User} with a given last name.
     */
    public List<User> getByLastName(String lastName) {
        if (lastName == null)
            return null;
        return userRepository.findByLastName(lastName);
    }

    /**
     * Return user with given Id.
     *
     * @param id of the user.
     * @return user with given id.
     * @throws InvalidIdException if the given ID was invalid
     */
    public User getById(String id) throws InvalidIdException {

        //Validating userID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "UserID \"" + id + "\" is not valid.";
            logger.debug(message);
            throw new InvalidIdException(message);
        }
        logger.trace("UserID \"{}\" is valid.", id);

        //Finding user by ID
        final User entity;
        try {
            logger.trace("Attempting to find user by ID \"{}\" ...", id);
            entity = userRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding user by ID \"{}\". Can't find user.", id);
            throw new InvalidIdException("Can't find user: " + e.getMessage());
        }

        logger.trace("Finding of user by ID \"{}\" completed without exceptions.", id);
        logger.info("Find user by ID \"{}\" - triggered by: {}", id, getExecutingUser().getUsername());
        logger.trace("Returning user by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return a List of users with for the List of given user ids.
     *
     * @param ids of the users to be fetched
<<<<<<< HEAD
     * @return the user by id
=======
     * @return all Users with the given IDs
>>>>>>> 462878d6549b28e851295ba416c492349e9396b6
     */
    public List<User> getByIds(Collection<String> ids) {
        logger.debug("Fetching users by ids.");
        if (ids == null) {
            logger.debug("Attempting to fetch users with ids = null. Aborting.");
            return new ArrayList<>();
        }
        if (ids.size() < 1) {
            logger.debug("Attempting to fetch users with empty ids. Aborting.");
            return new ArrayList<>();
        }

        ArrayList<User> result = new ArrayList<>();

        // Fetch user for every given id and skip if the id is invalid.
        for (String id : ids) {
            try {
                result.add(this.getById(id));
            } catch (InvalidIdException e) {
                logger.debug("User id: {}} was found invalid. Skipping...", id);
                continue;
            }
        }

        return result;
    }


    /**
     * Clear the cached information about a given user / subject in all KCBRealms.
     *
     * @param user the cache will be cleared for
     */
    private void clearRealmCache(final User user) {
        logger.trace("Clearing realm caches for user " + user.getUsername());
        try {
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            Collection<Realm> realms = securityManager.getRealms();

            for (Realm realm : realms) {
                if (realm.getClass().equals(KCBRealm.class)) {
                    logger.debug("Found {}. Will clear realm cache for {}", realm.getName(), user.getUsername());
                    ((KCBRealm) realm).refresh(user);
                }
            }
        } catch (RuntimeException e) {
            logger.warn("RuntimeException occurred during realm cache clearance. " + e);
        }
    }

    /**
     * Request a password reset for the user with the given id.
     *
     * @param eMail of the user whose password is to be reset
     */
    public void resetPassword(final String eMail) throws PasswordResetException {
        logger.trace("Reset password for {}", eMail);
        final User user = this.getByEmail(eMail);
        if (user == null) {
            final String exceptionMessage = "No user found for " + eMail + ". Password reset aborted.";
            logger.debug(exceptionMessage);
            throw new PasswordResetException(exceptionMessage);
        }
        this.resetOldResetTokens(user);
        final String token = ResetToken.generateToken();
        final String resetURL = this.generateResetURL(token);

        final ResetToken resetToken = new ResetToken();
        resetToken.setUser(user);
        // store hashed token in database (Use sha256 without hash)
        resetToken.setToken(Hashing.sha256().hashString(token, StandardCharsets.UTF_8).toString());
        resetToken.updateExpirationDate();
        try {
            emailUtil.sendResetMessage(resetURL, user);
        } catch (MessagingException e) {
            logger.debug("E-Mail delivery failed: {}", e.getMessage());
            throw new PasswordResetException("E-Mail delivery failed: " + e);
        }
        resetTokenRepository.save(resetToken);
    }

    /**
     * Generates an reset token for the given user
     *
     * @param user to generate reset token for
     * @return ResetToken generated for the given user with default expiration time
     * @throws PasswordResetException
     */
    public String generateResetToken(final User user) throws PasswordResetException {
        logger.trace("Reset password for {}", user);
        if (user == null) {
            final String exceptionMessage = "No user found for. Password reset aborted.";
            logger.debug(exceptionMessage);
            throw new PasswordResetException(exceptionMessage);
        }
        this.resetOldResetTokens(user);
        final String token = ResetToken.generateToken();

        final ResetToken resetToken = new ResetToken();
        resetToken.setUser(user);
        resetToken.setToken(Hashing.sha256().hashString(token, StandardCharsets.UTF_8).toString());
        resetToken.updateExpirationDate();
        resetTokenRepository.save(resetToken);
        return token;
    }

    /**
     * Checks if the given String is a valid and not expired reset token
     *
     * @param token to check
     * @return ResetToken that is stored if the given token is valid.
     * @throws InvalidTokenException if the given token is invalid
     */
    public ResetToken validateToken(final String token) throws InvalidTokenException {
        logger.trace("Validate token: {}", token);
        if (token == null || token.equals(""))
            throw new InvalidTokenException("Token can't be empty");
        String decodedToken = "";
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            decodedToken = new String(decodedBytes);
        } catch (RuntimeException e) {
            throw new InvalidTokenException(e.getMessage());
        }
        // since token is stored as sha256 we need to hash token before database lookup
        final String hashedToken = Hashing.sha256().hashString(decodedToken, StandardCharsets.UTF_8).toString();
        List<ResetToken> storedTokens = resetTokenRepository.findByToken(hashedToken);
        if (storedTokens == null || storedTokens.size() < 1)
            throw new InvalidTokenException("No entries found for given token.");
        ResetToken storedToken = storedTokens.get(0);
        // storedToken expiration date is smaller than current time -> token is expired
        boolean expired = storedToken.getExpirationDate().compareTo(LocalTime.now()) < 0;
        if (expired) {
            logger.debug("Given token is expired. Will not allow password reset for {}", storedToken.getUser().getUsername());
            throw new ExpiredTokenException("Given token is expired. Please create a new one.");
        }
        // token is not expired and is valid
        return storedToken;
    }

    /**
     * Confirm password change with given password. Will update the users password with the given password if
     * password and passwordRepeat match.
     *
     * @param password       new password to be set
     * @param passwordRepeat repeated password to verify no spelling mistakes were made
     */
    public void confirmPasswordChange(final String password, final String passwordRepeat, final String token) throws PasswordChangeException, InvalidTokenException {
        if (!password.equals(passwordRepeat)) {
            logger.debug("Password change failed. Given passwords didn't match.");
            throw new PasswordsNotMatchingException("Given passwords didn't match");
        }
        // validate token again since it can be expired by now
        ResetToken storedToken;
        try {
            storedToken = this.validateToken(token);
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException(e.getMessage());
        }
        final User user = storedToken.getUser();
        if (user == null) {
            logger.debug("Password change failed. Given user was null.");
            throw new InvalidTokenException("User was null.");
        }
        final String hashedPassword = hashPassword(password);
        user.setPassword(hashedPassword);
        try {
            this.update(user);
        } catch (UpdateException e) {
            final String message = "User update failed during password reset: " + e.getMessage();
            logger.debug(message);
            throw new PasswordChangeException(message);
        }
        this.resetTokenRepository.attachAndRemove(storedToken);
        this.login(user, password);
    }

    /**
     * Invalidate all reset tokens for the given user.
     *
     * @param user to invalidate tokens for
     */
    private void resetOldResetTokens(final User user) {
        final List<ResetToken> tokens = resetTokenRepository.findByUser(user);
        if (tokens == null || tokens.size() == 0)
            return;
        // remove all previously created tokens. Should be only one since tokens get deleted when
        // a new token is created
        for (ResetToken token : tokens)
            resetTokenRepository.attachAndRemove(token);
    }

    /**
     * Generate the password reset url with the given token
     *
     * @param token to be used for password reset
     * @return password reset url
     */
    public String generateResetURL(final String token) {
        final String params = "?token=" + Base64.getEncoder().encodeToString(token.getBytes());
        final String resetPath = "/password/confirm.xhtml";
        try {
            final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            final String baseAddress = request.getHeader("Origin") + request.getContextPath();
            return baseAddress + resetPath + params;
        } catch (RuntimeException e) {
            logger.warn("Couldn't get HttpServletRequest from ExternalContext. Will return default value.");
            return "http://localhost:8080" + resetPath + params;
        }
    }

    /**
     * Hash the given password using Scrypt hashing alogrithm and service default values.
     *
     * @param password to be hashed
     * @return hashed and salted version of the given password
     */
    private String hashPassword(final String password) {
        return SCryptUtil.scrypt(password, DEFAULT_SCRYPT_N, DEFAULT_SCRYPT_R, DEFAULT_SCRYPT_P);
    }

    /**
     * Logs the given user in with the given password. The password needs to be in plain text.
     *
     * @param user     to be logged in
     * @param password of the user to log in
     */
    private void login(final User user, final String password) throws AuthenticationException {
        final String username = user.getUsername();
        final UsernamePasswordToken authcToken = new UsernamePasswordToken();
        authcToken.setUsername(username);
        authcToken.setPassword(password.toCharArray());
        Subject subject = new Subject.Builder().buildSubject();
        subject.login(authcToken);
        logger.debug("Successfully logged in {}", user.getUsername());
    }

    /**
     * Stores the given object in the PU using Transactions.
     *
     * @param o to be persisted
     */
    public void save(Object o) {
        em.getTransaction().begin();
        if (!em.contains(o)) {
            em.persist(o);
            em.getTransaction().commit();
        }
    }

    /**
     * Sets toggle menu sidebar open or closed.
     */
    public User toggleMenu(User user) {
        user.setPinned(!user.isPinned());
        return userRepository.save(user);
    }

}
