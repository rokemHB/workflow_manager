package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Priority;

import java.util.regex.Pattern;

/**
 * Validator to validate Priorities.
 * @see Priority
 *
 * @author Robin
 * @author Marius
 */
public class PriorityValidator extends PatternValidator<Priority> {
    /**
     * Validates a Priority
     * @param instance priority to be validated
     * @return true if priority is valid, false if priority is invalid
     * @throws ValidationException if validation fails
     */
    @Override
    public boolean validate(Priority instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getName() == null)
            throw new ValidationNullPointerException("Name of instance " + instance.toString() + " is null.");

        final Pattern pattern = validatorConfig.getPattern("PriorityName");

        return checkMatch(pattern, instance.getName());
    }
}
