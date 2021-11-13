package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test Class to Test {@link ValueValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class ValueValidatorTest extends ValidatorTest {
    /**
     * ValueValidator to be tested
     */
    @InjectMocks
    private ValueValidator valueValidator;

    /**
     * Setup test environment
     */
    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * Test Validator with an empty value
     */
    @Test(expected = ValidationException.class)
    public void testNullValue() throws ValidationException {
        valueValidator.validate(null);
    }

    /**
     * Test Validator with valid value
     */
    @Test
    public void testValidValue() throws ValidationException {
        Value value = new Value();
        value.setId("1");
        value.setParameter(new Parameter());
        value.setValue("Test");
        assertTrue(valueValidator.validate(value));
        verify(validatorConfig).getPattern("Value");
    }

    /**
     * Test Validator with value with null parameter
     */
    @Test(expected = ValidationException.class)
    public void testValueNullParameter() throws ValidationException {
        Value value = new Value();
        value.setId("1");
        value.setParameter(null);
        value.setValue("Test");
        valueValidator.validate(value);
    }

    /**
     * Test Validator with value with null parameter
     */
    @Test(expected = ValidationException.class)
    public void testValueNullValue() throws ValidationException {
        Value value = new Value();
        value.setId("1");
        value.setParameter(new Parameter());
        value.setValue(null);
        valueValidator.validate(value);
    }

}
