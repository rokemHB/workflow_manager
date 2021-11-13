package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test class to test {@link LocationValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class LocationValidatorTest extends ValidatorTest {
    /**
     * LocationValidator to be tested
     */
    @InjectMocks
    private LocationValidator locationValidator;

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
    public void testNullLocation() throws ValidationNullPointerException {
        locationValidator.validate(null);
    }

    /**
     * Test Validator with valid location
     */
    @Test
    public void testValidLocation() throws ValidationException {
        Location stock = new Stock();
        stock.setPosition("Lager");
        assertTrue(stock.toString() + " should pass validation.", locationValidator.validate(stock));
        verify(validatorConfig).getPattern("Position");
    }

    /**
     * Test Validator with location with null position
     */
    @Test(expected = ValidationException.class)
    public void testLocationNullPosition() throws ValidationNullPointerException {
        Location stock = new Stock();
        stock.setPosition(null);
        locationValidator.validate(stock);
    }
}
