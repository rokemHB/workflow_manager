package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Job;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Validator to validate Job.
 *
 * @see Job
 *
 * @author Robin
 * @author Marius
 */
public class JobValidator extends PatternValidator<Job> {

    /**
     * Logger object of the JobValidator class
     */
    private static final Logger logger = LogManager.getLogger(JobValidator.class);

    /**
     * Validates a Job
     *
     * @param instance Job to be validated
     * @return true if Job is valid, false if Job is invalid
     * @throws ValidationException if validation fails
     */
    @Override
    public boolean validate(Job instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getName() == null)
            throw new ValidationNullPointerException("Name of instance " + instance.toString() + " is null.");
        if (instance.getPriority() == null)
            throw new ValidationNullPointerException("Priority of instance " + instance.toString() + " is null.");
        if (instance.getProcessChain() == null)
            throw new ValidationNullPointerException("ProcessChain of instance " + instance.toString() + " is null.");

        final Pattern jobNamePattern = validatorConfig.getPattern("JobName");
        logger.debug("validating Job");

        return checkMatch(jobNamePattern, instance.getName());
    }
}
