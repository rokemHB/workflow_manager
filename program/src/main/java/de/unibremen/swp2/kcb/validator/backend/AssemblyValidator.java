package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Assembly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Validator to validate Assemblies.
 *
 * @see Assembly
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 */
public class AssemblyValidator extends PatternValidator<Assembly> {

    /**
     * Logger object of the AssemblyValidator class
     */
    private static final Logger logger = LogManager.getLogger(AssemblyValidator.class);

    /**
     * Validates an Assembly
     *
     * @param instance sample to be validated
     * @return true if sample is valid, false if sample is invalid
     */
    @Override
    public boolean validate(Assembly instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getAlloy() == null)
            throw new ValidationNullPointerException("Alloy of instance " + instance.toString() + " is null.");
        if (instance.getCarriers() == null || instance.getCarriers().isEmpty())
            throw new ValidationNullPointerException("Carrier of instance " + instance.toString() + " is null.");
        if (instance.getComment() == null)
            logger.debug("Comment of {} is null.", instance);
        if (instance.getModifications() == null)
            logger.debug("Modifications of {} are null.", instance);
        if (instance.getPositionAtCarrier() == null)
            logger.debug("PositionAtCarrier of {} is null.", instance);

        final Pattern idPattern = validatorConfig.getPattern("AssemblyId");
        final Pattern alloyPattern = validatorConfig.getPattern("AssemblyAlloy");

        return checkMatch(idPattern, instance.getAssemblyID()) && checkMatch(alloyPattern, instance.getAlloy());
    }
}
