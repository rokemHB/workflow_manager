package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertTrue;

/**
 * Test class to test {@link CarrierTypeValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class CarrierTypeValidatorTest extends ValidatorTest {

    /**
     * CarrierTypeValidator to be tested
     */
    @InjectMocks
    private CarrierTypeValidator carrierTypeValidator;

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
    public void testNullLocation() throws ValidationException {
        carrierTypeValidator.validate(null);
    }

    /**
     * Test Validator with valid carrierType
     */
    @Test
    public void testValidCarrierType() throws ValidationException {
        CarrierType carrierType = new CarrierType();
        carrierType.setName("CT-01");
        assertTrue(carrierType.toString() + " should pass validation.", carrierTypeValidator.validate(carrierType));
    }

    /**
     * Test Validator with location with null position
     */
    @Test(expected = ValidationException.class)
    public void testCarrierTypeNullName() throws ValidationException {
        CarrierType carrierType = new CarrierType();
        carrierType.setName(null);
        carrierTypeValidator.validate(carrierType);
    }
}
