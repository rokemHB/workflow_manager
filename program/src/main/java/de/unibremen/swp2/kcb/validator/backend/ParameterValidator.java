package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.parameter.Parameter;

import java.util.regex.Pattern;

/**
 * Validator to validate Parameters.
 *
 * @see Parameter
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class ParameterValidator extends PatternValidator<Parameter> {
    /**
     * Validates a Parameter
     *
     * @param instance Parameter to be validated
     * @return true if Parameter is valid, false if Parameter is invalid
     * @throws ValidationException if validation fails
     */
    @Override
    public boolean validate(Parameter instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getField() == null)
            throw new ValidationNullPointerException("Field of instance " + instance.toString() + " is null.");

        final Pattern pattern = validatorConfig.getPattern("ParameterField");

        return checkMatch(pattern, instance.getField());
    }
}
