package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.ProcessStep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Validator to validate ProcessSteps.
 *
 * @see ProcessStep
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
public class ProcessStepValidator extends PatternValidator<ProcessStep> {
    /**
     * Logger object of the ProcessStep class
     */
    private static final Logger logger = LogManager.getLogger(ProcessStepValidator.class);

    /**
     * Validates a ProcessStep
     *
     * @param instance processStep to be validated
     * @return true if processStep is valid, false if processStep is invalid
     */
    @Override
    public boolean validate(ProcessStep instance) throws ValidationNullPointerException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getName() == null)
            throw new ValidationNullPointerException("Name of instance " + instance.toString() + " is null.");

        if (instance.getParameters() == null)
            throw new ValidationNullPointerException("Parameters of instance " + instance.toString() + " is null.");
        if (instance.getStateMachine() == null)
            throw new ValidationNullPointerException("StateMachine of instance " + instance.toString() + " is null.");
        if (instance.getWorkstation() == null)
            throw new ValidationNullPointerException("Workstation of instance " + instance.toString() + " is null.");
        if (instance.getPreparation() == null)
            logger.debug("Preparation of {} is null.", instance);
        if (instance.getOutput() == null)
            logger.debug("Output of {} is null", instance);

        final Pattern pattern = validatorConfig.getPattern("ProcessStepName");

        return checkMatch(pattern, instance.getName());
    }
}
