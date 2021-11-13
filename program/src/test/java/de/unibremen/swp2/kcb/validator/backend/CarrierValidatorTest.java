package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Class CarrierValidatorTest
 *
 * @author Marc
 * @author Marius
 * @author Sören
 */
public class CarrierValidatorTest extends ValidatorTest {

    /**
     * CarrierValidator to be tested
     */
    @InjectMocks
    private CarrierValidator carrierValidator;

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
    public void testNullCarrier() throws ValidationException {
        carrierValidator.validate(null);
    }

//    /**
//     * Test Validator with valid carrier
//     */
//    @Test
//    public void testValidCarrier() throws ValidationException {
//        Carrier carrier = new Carrier();
//        carrier.setCarrierType(new CarrierType());
//        carrier.setCarrierID("C-01");
//        carrier.setLocation(new Stock());
//        assertTrue(carrier.toString() + " should pass validation.", carrierValidator.validate(carrier));
//        verify(validatorConfig).getPattern("carrierId");
//    }
//
//    /**
//     * Test Validator with carrier with null carrierType
//     */
//    @Test(expected = ValidationException.class)
//    public void testCarrierNullType() throws ValidationException {
//        Carrier carrier = new Carrier();
//        carrier.setCarrierType(null);
//        carrier.setCarrierID("C-01");
//        carrier.setLocation(new Stock());
//        carrierValidator.validate(carrier);
//    }
//
//    /**
//     * Test Validator with carrier with null id
//     */
//    @Test(expected = ValidationException.class)
//    public void testCarrierNullId() throws ValidationException {
//        Carrier carrier = new Carrier();
//        carrier.setCarrierType(new CarrierType());
//        carrier.setCarrierID(null);
//        carrier.setLocation(new Stock());
//        carrierValidator.validate(carrier);
//    }

    /**
     * Test Validator with carrier with null id
     */
    @Test(expected = ValidationException.class)
    public void testCarrierNullLocation() throws ValidationException {
        Carrier carrier = new Carrier();
        carrier.setCarrierType(new CarrierType());
        carrier.setCarrierID("C-01");
        carrier.setLocation(null);
        carrierValidator.validate(carrier);
    }
}
