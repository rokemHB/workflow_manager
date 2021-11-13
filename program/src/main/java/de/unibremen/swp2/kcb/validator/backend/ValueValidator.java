package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.parameter.Value;

import java.util.regex.Pattern;

/**
 * Validator to validate Values.
 *
 * @see Value
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class ValueValidator extends PatternValidator<Value> {
    /**
     * Validates a Value
     *
     * @param instance Value to be validated
     * @return true if Value is valid, false if Value is invalid
     */
    @Override
    public boolean validate(Value instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getValue() == null)
            throw new ValidationNullPointerException("Value of instance " + instance.toString() + " is null.");
        if (instance.getParameter() == null)
            throw new ValidationNullPointerException("Parameter of instance " + instance.toString() + " is null.");

        final Pattern pattern = validatorConfig.getPattern("Value");

        return checkMatch(pattern, instance.getValue());
    }
}
