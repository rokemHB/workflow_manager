package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Carrier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Validator to validate Carriers.
 *
 * @see Carrier
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class CarrierValidator extends PatternValidator<Carrier> {

    /**
     * Logger object of the CarrierValidator class
     */
    private static final Logger logger = LogManager.getLogger(CarrierValidator.class);

    /**
     * Validates a Carrier
     *
     * @param instance carrier to be validated
     * @return true if carrier is valid, false if carrier is invalid
     * @throws ValidationException if validation fails
     */
    @Override
    public boolean validate(Carrier instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getCarrierType() == null)
            throw new ValidationNullPointerException("CarrierType of instance " + instance.getId() + " is null.");
        if (instance.getLocation() == null)
            throw new ValidationNullPointerException("Location of instace " + instance.getId() + " is null.");
        if (instance.getCarrierID() == null)
            throw new ValidationNullPointerException("CarrierID of instace " + instance.getId() + " is null.");

        final Pattern carrierIdPattern = validatorConfig.getPattern("CarrierId");
        logger.debug("validating carrier");

        return checkMatch(carrierIdPattern, instance.getCarrierID());
    }
}
