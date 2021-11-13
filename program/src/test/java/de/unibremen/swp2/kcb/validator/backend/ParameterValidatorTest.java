package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test class to test {@link ParameterValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class ParameterValidatorTest extends ValidatorTest {
    /**
     * StateValidator to be tested
     */
    @InjectMocks
    private ParameterValidator parameterValidator;

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
        parameterValidator.validate(null);
    }

    /**
     * Test Validator with valid parameter
     */
    @Test
    public void testValidParameter() throws ValidationException {
        Parameter parameter = new Parameter();
        parameter.setField("PM-Field1");
        assertTrue(parameter.toString() + " should pass validation.", parameterValidator.validate(parameter));
        verify(validatorConfig).getPattern("ParameterField");
    }

    /**
     * Test Validator with parameter with null field
     */
    @Test(expected = ValidationException.class)
    public void testParameterNullField() throws ValidationException {
        Parameter parameter = new Parameter();
        parameter.setField(null);
        parameterValidator.validate(parameter);
    }
}
