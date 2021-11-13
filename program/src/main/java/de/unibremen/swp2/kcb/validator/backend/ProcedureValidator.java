package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Procedure;

/**
 * Validator to validate Procedures.
 *
 * @see Procedure
 *
 * @author Marius
 * @author Sören
 */
public class ProcedureValidator implements Validator<Procedure> {
    /**
     * Validates a Procedure
     *
     * @param instance Procedure to be validated
     * @return true if Procedure is valid, false if Procedure is invalid
     */
    @Override
    public boolean validate(Procedure instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getProcessStep() == null)
            throw new ValidationNullPointerException("ProcessStep of instance " + instance.toString() + " is null.");
        if (instance.getStateHistory() == null)
            throw new ValidationNullPointerException("StateHistory of instance " + instance.toString() + " is null.");
        if (instance.getValues() == null)
            throw new ValidationNullPointerException("Values of instance " + instance.toString() + " is null.");

        return true;
    }
}
