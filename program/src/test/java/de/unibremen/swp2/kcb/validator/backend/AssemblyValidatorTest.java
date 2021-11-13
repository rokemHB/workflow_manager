package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test class to test {@link AssemblyValidator}
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 */
public class AssemblyValidatorTest extends ValidatorTest {

    /**
     * AssemblyValidator to be tested
     */
    @InjectMocks
    private AssemblyValidator assemblyValidator;

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
    public void testNullAssembly() throws ValidationException {
        assemblyValidator.validate(null);
    }

    /**
     * Test Validator with valid location
     */
    @Test
    public void testValidAssembly() throws ValidationException {
        Assembly assembly = new Assembly();
        List<Carrier> carriers = new ArrayList<>();
        Carrier carrier = new Carrier();
        carriers.add(carrier);
        assembly.setAlloy("A-01");
        assembly.setCarriers(carriers);
        assembly.setModifications(null);
        assembly.setComment(null);
        assembly.setPositionAtCarrier(null);
        assembly.setAssemblyID("123");
        assertTrue(assembly + " should pass validation.", assemblyValidator.validate(assembly));
        verify(validatorConfig).getPattern("AssemblyId");
    }

    /**
     * Test Validator with sample with null carrier
     */
    @Test(expected = ValidationException.class)
    public void testAssemblyNullCarrier() throws ValidationException {
        Assembly sample = new Assembly();
        sample.setAlloy("A-01");
        sample.setCarriers(null);
        sample.setModifications(new ArrayList<>());
        sample.setComment("Comment");
        sample.setPositionAtCarrier("P-01");
        assemblyValidator.validate(sample);
    }

    /**
     * Test Validator with sample with null alloy
     */
    @Test(expected = ValidationException.class)
    public void testAssemblyNullAlloy() throws ValidationException {
        Assembly sample = new Assembly();
        List<Carrier> carriers = new ArrayList<>();
        Carrier carrier = new Carrier();
        carriers.add(carrier);
        sample.setAlloy(null);
        sample.setCarriers(carriers);
        sample.setModifications(new ArrayList<>());
        sample.setAssemblyID("A01.1.1");
        sample.setComment("Comment");
        assemblyValidator.validate(sample);
    }
}
