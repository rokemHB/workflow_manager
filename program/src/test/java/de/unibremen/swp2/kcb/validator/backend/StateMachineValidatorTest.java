package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test class to test {@link StateMachineValidator}
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
public class StateMachineValidatorTest extends ValidatorTest {
    /**
     * StateMachineValidator to be tested
     */
    @InjectMocks
    private StateMachineValidator stateMachineValidator;

    /**
     * Setup test environment
     */
    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * Test Validator with null instance
     */
    @Test(expected = ValidationException.class)
    public void testNullStateMachine() throws ValidationException {
        stateMachineValidator.validate(null);
    }

    /**
     * Test Validator with valid stateMachine
     */
    @Test
    public void testValidState() throws ValidationException {
        StateMachine stateMachine = new StateMachine();
        List<State> stateList = new ArrayList<>();
        stateList.add(new State());
        stateMachine.setName("SM-01");
        stateMachine.setStateList(stateList);
        assertTrue(stateMachine.toString() + " should pass validation.", stateMachineValidator.validate(stateMachine));
        verify(validatorConfig).getPattern("StateMachineName");
    }

    /**
     * Test Validator with StateMachine with null name
     */
    @Test(expected = ValidationException.class)
    public void testStateMachineNullName() throws ValidationException {
        StateMachine stateMachine = new StateMachine();
        stateMachine.setName(null);
        stateMachine.setStateList(new ArrayList<>());
        stateMachineValidator.validate(stateMachine);
    }

    /**
     * Test StateMachineValidator with null StateList
     */
    @Test(expected = ValidationException.class)
    public void testStateMachineNullStateList() throws ValidationException {
        StateMachine stateMachine = new StateMachine();
        stateMachine.setName("SM-01");
        stateMachine.setStateList(null);
        stateMachineValidator.validate(stateMachine);
    }
}
