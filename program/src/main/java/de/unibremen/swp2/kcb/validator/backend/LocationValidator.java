package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Locations.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Validator to validate Location.
 * @see Location
 *
 * @author Marc
 * @author Robin
 * @author Marius
 */
public class LocationValidator extends PatternValidator<Location> {

    /**
     * Logger object of the LocationValidator class
     */
    private static final Logger logger = LogManager.getLogger(LocationValidator.class);

    /**
     * Validates a Location
     * @param instance Location to be validated
     * @return true if Location is valid, false if Location is invalid
     * @throws ValidationNullPointerException if validation fails
     */
    @Override
    public boolean validate(Location instance) throws ValidationNullPointerException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getPosition() == null)
            throw new ValidationNullPointerException("Position of instance " + instance.toString() + " is null.");

        final Pattern pattern = validatorConfig.getPattern("Position");

        return checkMatch(pattern, instance.getPosition());
    }
}
