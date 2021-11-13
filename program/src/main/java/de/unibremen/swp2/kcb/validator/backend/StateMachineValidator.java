package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;

import java.util.regex.Pattern;

/**
 * Validator to validate StateMachines.
 *
 * @see StateMachine
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
public class StateMachineValidator extends PatternValidator<StateMachine> {

    /**
     * Validates a StateMachines
     *
     * @param instance StateMachine to be validated
     * @return true if StateMachine is valid, false if StateMachine is invalid
     */
    @Override
    public boolean validate(StateMachine instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getName() == null)
            throw new ValidationNullPointerException("Name of instance " + instance.toString() + " is null.");
        if (instance.getStateList() == null || instance.getStateList().isEmpty())
            throw new ValidationNullPointerException("StateList of instance " + instance.toString() + " is null.");

        final Pattern pattern = validatorConfig.getPattern("StateMachineName");

        return checkMatch(pattern, instance.getName());
    }
}
