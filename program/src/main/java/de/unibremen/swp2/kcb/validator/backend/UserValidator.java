package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Validator to validate Users.
 *
 * @see User
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 */
public class UserValidator extends PatternValidator<User> {
    /**
     * Logger object of the UserValidator class
     */
    private static final Logger logger = LogManager.getLogger(UserValidator.class);

    /**
     * Validates a User
     *
     * @param instance User to be validated
     * @return true if User is valid, false if User is invalid
     * @throws ValidationException if validation fails
     */
    @Override
    public boolean validate(User instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered user was null");
        if (instance.getRoles() == null)
            throw new ValidationNullPointerException("Roles of instance " + instance.toString() + " are null.");
        if (instance.getEmail() == null)
            throw new ValidationNullPointerException("Email of instance " + instance.toString() + " is null.");
        if (instance.getFirstName() == null)
            throw new ValidationNullPointerException("FirstName of instance " + instance.toString() + " is null.");
        if (instance.getPassword() == null)
            throw new ValidationNullPointerException("Password of instance " + instance.toString() + " is null.");
        if (instance.getUsername() == null)
            throw new ValidationNullPointerException("Username of instance " + instance.toString() + " is null.");
        if (instance.getLastName() == null)
            throw new ValidationNullPointerException("LastName of instance " + instance.toString() + " is null.");

        final Pattern usernamePattern = validatorConfig.getPattern("Username");
        final Pattern emailPattern = validatorConfig.getPattern("Email");
        final Pattern namePattern = validatorConfig.getPattern("Name");

        if (instance.getRoles().isEmpty()) {
            logger.debug("Validation failed. User roles are empty");
            return false;
        }

        return checkMatch(usernamePattern, instance.getUsername())
                && checkMatch(emailPattern, instance.getEmail())
                && checkMatch(namePattern, instance.getFirstName())
                && checkMatch(namePattern, instance.getLastName());
    }


}
