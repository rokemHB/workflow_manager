package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.parameter.Value;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Test class to test {@link ProcedureValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class ProcedureValidatorTest {
    /**
     * Procedure to be tested
     */
    private ProcedureValidator procedureValidator;

    /**
     * Setup test environment
     */
    @Before
    public void setUp() {
        this.procedureValidator = new ProcedureValidator();
    }

    /**
     * Test Validator with null instance
     */
    @Test(expected = ValidationException.class)
    public void testNullProcedure() throws ValidationException {
        procedureValidator.validate(null);
    }

    /**
     * Test Validator with valid procedure
     */
    @Test
    public void testValidProcedure() throws ValidationException {
        Procedure procedure = new Procedure();
        procedure.setProcessStep(new ProcessStep());
        procedure.setStateHistory(new StateHistory());
        procedure.setValues(new ArrayList<Value>());
        assertTrue(procedure.toString() + " should pass validation.", procedureValidator.validate(procedure));
    }

    /**
     * Test Validator with procedure with null processStep
     */
    @Test(expected = ValidationException.class)
    public void testProcedureNullProcessStep() throws ValidationException {
        Procedure procedure = new Procedure();
        procedure.setProcessStep(null);
        procedure.setStateHistory(new StateHistory());
        procedure.setValues(new ArrayList<Value>());
        procedureValidator.validate(procedure);
    }

    /**
     * Test Validator with procedure with null stateHistory
     */
    @Test(expected = ValidationException.class)
    public void testProcedureNullStateHistory() throws ValidationException {
        Procedure procedure = new Procedure();
        procedure.setProcessStep(new ProcessStep());
        procedure.setStateHistory(null);
        procedure.setValues(new ArrayList<Value>());
        procedureValidator.validate(procedure);
    }

    /**
     * Test Validator with procedure with null values
     */
    @Test(expected = ValidationException.class)
    public void testProcedureNullValues() throws ValidationException {
        Procedure procedure = new Procedure();
        procedure.setProcessStep(new ProcessStep());
        procedure.setStateHistory(new StateHistory());
        procedure.setValues(null);
        procedureValidator.validate(procedure);
    }
}
