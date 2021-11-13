package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.CarrierType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Validator to validate CarrierTypes.
 *
 * @see CarrierType
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
public class CarrierTypeValidator extends PatternValidator<CarrierType> {

    /**
     * Logger object of the CarrierType class
     */
    private static final Logger logger = LogManager.getLogger(CarrierTypeValidator.class);

    /**
     * Validates a CarrierType
     *
     * @param instance CarrierType to be validated
     * @return true if CarrierType is valid, false if CarrierType is invalid
     * @throws ValidationException if validation fails
     */
    @Override
    public boolean validate(CarrierType instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getName() == null)
            throw new ValidationNullPointerException("Name of instance " + instance.toString() + " is null.");

        final Pattern carrierTypePattern = validatorConfig.getPattern("CarrierType");
        logger.debug("validating carrierType");

        return checkMatch(carrierTypePattern, instance.getName());
    }
}
