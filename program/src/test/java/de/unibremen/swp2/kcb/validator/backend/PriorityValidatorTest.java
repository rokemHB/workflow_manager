package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test class to test {@link PriorityValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class PriorityValidatorTest extends ValidatorTest {

    /**
     * PriorityValidator to be tested
     */
    @InjectMocks
    private PriorityValidator priorityValidator;

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
    public void testNullPriority() throws ValidationException {
        priorityValidator.validate(null);
    }

    /**
     * Test Validator with valid priority
     */
    @Test
    public void testValidPriority() throws ValidationException {
        Priority priority = new Priority();
        priority.setName("Prio1");
        priority.setValue(10);
        assertTrue(priority.toString() + " should pass validation.", priorityValidator.validate(priority));
        verify(validatorConfig).getPattern("PriorityName");
    }

    /**
     * Test Validator with state with null name
     */
    @Test(expected = ValidationException.class)
    public void testPriorityNullName() throws ValidationException {
        Priority priority = new Priority();
        priority.setName(null);
        priority.setValue(10);
        priorityValidator.validate(priority);
    }
}
