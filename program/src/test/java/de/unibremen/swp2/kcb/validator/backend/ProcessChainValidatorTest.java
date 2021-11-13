package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test class to test {@link ProcessChainValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class ProcessChainValidatorTest extends ValidatorTest {
    /**
     * ProcessChainValidator to be tested
     */
    @InjectMocks
    private ProcessChainValidator processChainValidator;

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
    public void testNullProcessChain() throws ValidationException {
        processChainValidator.validate(null);
    }

    /**
     * Test Validator with valid processChain
     */
    @Test
    public void testValidProcessChain() throws ValidationException {
        ArrayList<ProcessStep> chain = new ArrayList<>();
        chain.add(new ProcessStep());
        ProcessChain processChain = new ProcessChain();
        processChain.setChain(chain);
        processChain.setName("PChain-01");
        assertTrue(processChain.toString() + " should pass validation.", processChainValidator.validate(processChain));
        verify(validatorConfig).getPattern("ProcessChainName");
    }

    /**
     * Test ProcessChainValidator with null name
     */
    @Test(expected = ValidationException.class)
    public void testStateNullName() throws ValidationException {
        ArrayList<ProcessStep> chain = new ArrayList<>();
        chain.add(new ProcessStep());
        ProcessChain processChain = new ProcessChain();
        processChain.setChain(chain);
        processChain.setName(null);
        processChainValidator.validate(processChain);
    }

    /**
     * Test ProcessChainValidator with null chain
     */
    @Test(expected = ValidationException.class)
    public void testStateNullChain() throws ValidationException {
        ProcessChain processChain = new ProcessChain();
        processChain.setChain(null);
        processChain.setName("PChain-01");
        processChainValidator.validate(processChain);
    }
}
