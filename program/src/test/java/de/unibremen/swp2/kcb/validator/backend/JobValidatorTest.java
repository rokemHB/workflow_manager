package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test class to test {@link JobValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class JobValidatorTest extends ValidatorTest {

    /**
     * JobValidator to be tested
     */
    @InjectMocks
    private JobValidator jobValidator;

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
    public void testNullJob() throws ValidationException {
        jobValidator.validate(null);
    }

    /**
     * Test Validator with valid location
     */
    @Test
    public void testValidJob() throws ValidationException {
        Job job = new Job();
        job.setAssemblies(new ArrayList<Assembly>());
        job.setName("J-01");
        job.setPriority(new Priority());
        job.setProcessChain(new ProcessChain());
        assertTrue(job.toString() + " should pass validation.", jobValidator.validate(job));
        verify(validatorConfig).getPattern("JobName");
    }

    /**
     * Test Validator with job with null name
     */
    @Test(expected = ValidationException.class)
    public void testJobNullName() throws ValidationException {
        Job job = new Job();
        job.setAssemblies(new ArrayList<>());
        job.setName(null);
        job.setPriority(new Priority());
        job.setProcessChain(new ProcessChain());
        jobValidator.validate(job);
    }

    /**
     * Test Validator with job with null priority
     */
    @Test(expected = ValidationException.class)
    public void testJobNullPriority() throws ValidationException {
        Job job = new Job();
        job.setAssemblies(new ArrayList<>());
        job.setName("J-01");
        job.setPriority(null);
        job.setProcessChain(new ProcessChain());
        jobValidator.validate(job);
    }

    /**
     * Test Validator with job with null processChain
     */
    @Test(expected = ValidationException.class)
    public void testJobNullProcessChain() throws ValidationException {
        Job job = new Job();
        job.setAssemblies(new ArrayList<>());
        job.setName("J-01");
        job.setPriority(new Priority());
        job.setProcessChain(null);
        jobValidator.validate(job);
    }
}
