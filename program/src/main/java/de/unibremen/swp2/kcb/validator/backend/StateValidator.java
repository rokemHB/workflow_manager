package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.StateMachine.State;

import java.util.regex.Pattern;

/**
 * Validator to validate States.
 *
 * @see State
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class StateValidator extends PatternValidator<State> {
    /**
     * Validates a State
     *
     * @param instance State to be validated
     * @return true if State is valid, false if State is invalid
     * @throws ValidationException if validation fails
     */
    @Override
    public boolean validate(State instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getName() == null)
            throw new ValidationNullPointerException("Name of instance " + instance.toString() + " is null.");

        final Pattern pattern = validatorConfig.getPattern("StateName");

        return checkMatch(pattern, instance.getName());
    }
}
