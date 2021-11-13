package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Validator to validate Workstations.
 *
 * @see Workstation
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
public class WorkstationValidator extends PatternValidator<Workstation> {

    /**
     * Logger object of the UserValidator class
     */
    private static final Logger logger = LogManager.getLogger(WorkstationValidator.class);

    /**
     * Validates a Workstation
     *
     * @param instance Workstation to be validated
     * @return true if Workstation is valid, false if Workstation is invalid
     * @throws ValidationException if validation fails
     */
    @Override
    public boolean validate(Workstation instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getName() == null)
            throw new ValidationNullPointerException("Name of instance " + instance.toString() + " is null.");
        if (instance.getBroken() == null)
            throw new ValidationNullPointerException("Broken attribute of instance " + instance.toString() + " is null.");
        if (instance.getUsers() == null)
            throw new ValidationNullPointerException("Users attribute of instance " + instance.toString() + " is null.");
        if (instance.getPosition() == null || instance.getPosition().isEmpty())
            throw new ValidationNullPointerException("Position attribute of instance " + instance.toString() + " is null or empty.");
        if (instance.getUsers().isEmpty())
            logger.debug("Validated Workstation {} has no users.", instance);

        final Pattern pattern = validatorConfig.getPattern("WorkstationName");

        return checkMatch(pattern, instance.getName());
    }
}
