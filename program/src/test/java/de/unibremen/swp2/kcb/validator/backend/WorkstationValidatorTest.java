package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test Class to Test {@link WorkstationValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class WorkstationValidatorTest extends ValidatorTest {

    /**
     * WorkstationValidator to be tested
     */
    @InjectMocks
    private WorkstationValidator workstationValidator;

    /**
     * Setup test environment
     */
    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * Test Validator with an null workstation
     */
    @Test(expected = ValidationException.class)
    public void testNullWorkstation() throws ValidationException {
        workstationValidator.validate(null);
    }

    /**
     * Test Validator with valid workstation
     */
    @Test
    public void testValidWorkstation() throws ValidationException {
        Workstation workstation = new Workstation();
        workstation.setName("WS-01");
        workstation.setPosition("testPosition");
        workstation.setId("1");
        workstation.setBroken(false);
        workstation.setUsers(new ArrayList<>());
        assertTrue(workstationValidator.validate(workstation));
        verify(validatorConfig).getPattern("WorkstationName");
    }

    /**
     * Test Validator with workstation with null name
     */
    @Test(expected = ValidationException.class)
    public void testWorkstationNullName() throws ValidationException {
        Workstation workstation = new Workstation();
        workstation.setName(null);
        workstation.setId("1");
        workstation.setBroken(false);
        workstation.setUsers(new ArrayList<>());
        workstationValidator.validate(workstation);
    }

    /**
     * Test Validator with workstation with null broken
     */
    @Test(expected = ValidationException.class)
    public void testWorkstationNullBroken() throws ValidationException {
        Workstation workstation = new Workstation();
        workstation.setName("WS-01");
        workstation.setId("1");
        workstation.setBroken(null);
        workstation.setUsers(new ArrayList<>());
        workstationValidator.validate(workstation);
    }

    /**
     * Test Validator with workstation with null users
     */
    @Test(expected = ValidationException.class)
    public void testWorkstationNullUsers() throws ValidationException {
        Workstation workstation = new Workstation();
        workstation.setName("WS-01");
        workstation.setId("1");
        workstation.setBroken(false);
        workstation.setUsers(null);
        workstationValidator.validate(workstation);
    }
}
