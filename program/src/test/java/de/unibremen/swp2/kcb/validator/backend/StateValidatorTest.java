package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test class to test {@link StateValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class StateValidatorTest extends ValidatorTest {

    /**
     * StateValidator to be tested
     */
    @InjectMocks
    private StateValidator stateValidator;

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
    public void testNullState() throws ValidationException {
        stateValidator.validate(null);
    }

    /**
     * Test Validator with valid state
     */
    @Test
    public void testValidState() throws ValidationException {
        State state = new State();
        state.setBlocking(true);
        state.setName("Working");
        state.setId("1");
        assertTrue(state.toString() + " should pass validation.", stateValidator.validate(state));
        verify(validatorConfig).getPattern("StateName");
    }

    /**
     * Test Validator with state with null name
     */
    @Test(expected = ValidationException.class)
    public void testStateNullName() throws ValidationException {
        State state = new State();
        state.setBlocking(true);
        state.setName(null);
        state.setId("1");
        stateValidator.validate(state);
    }
}
