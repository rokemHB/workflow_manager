package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test class to test {@link ProcessStepValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class ProcessStepValidatorTest extends ValidatorTest {
    /**
     * ProcessStepValidator to be tested
     */
    @InjectMocks
    private ProcessStepValidator processStepValidator;

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
    public void testNullProcessStep() throws ValidationException {
        processStepValidator.validate(null);
    }

    /**
     * Test Validator with valid processStep
     */
    @Test
    public void testValidProcessStep() throws ValidationException {
        ProcessStep processStep = new ProcessStep();
        processStep.setName("PC-01");
        processStep.setParameters(new ArrayList<Parameter>());
        processStep.setStateMachine(new StateMachine());
        processStep.setWorkstation(new Workstation());
        processStep.setOutput(new CarrierType());
        processStep.setPreparation(new CarrierType());
        assertTrue(processStep.toString() + " should pass validation.", processStepValidator.validate(processStep));
        verify(validatorConfig).getPattern("ProcessStepName");
    }

    /**
     * Test Validator with valid processStep
     */
    @Test
    public void testValidProcessStepNullPreparation() throws ValidationException {
        ProcessStep processStep = new ProcessStep();
        processStep.setName("PC-01");
        processStep.setParameters(new ArrayList<Parameter>());
        processStep.setStateMachine(new StateMachine());
        processStep.setWorkstation(new Workstation());
        processStep.setOutput(new CarrierType());
        processStep.setPreparation(null);
        assertTrue(processStep.toString() + " should pass validation.", processStepValidator.validate(processStep));
        verify(validatorConfig).getPattern("ProcessStepName");
    }

    /**
     * Test Validator with valid processStep
     */
    @Test
    public void testValidProcessStepNullOutput() throws ValidationException {
        ProcessStep processStep = new ProcessStep();
        processStep.setName("PC-01");
        processStep.setParameters(new ArrayList<Parameter>());
        processStep.setStateMachine(new StateMachine());
        processStep.setWorkstation(new Workstation());
        processStep.setOutput(null);
        processStep.setPreparation(new CarrierType());
        assertTrue(processStep.toString() + " should pass validation.", processStepValidator.validate(processStep));
        verify(validatorConfig).getPattern("ProcessStepName");
    }

    /**
     * Test Validator with processStep with null name
     */
    @Test(expected = ValidationException.class)
    public void testProcessStepNullName() throws ValidationException {
        ProcessStep processStep = new ProcessStep();
        processStep.setName(null);
        processStep.setParameters(new ArrayList<Parameter>());
        processStep.setStateMachine(new StateMachine());
        processStep.setWorkstation(new Workstation());
        processStep.setOutput(new CarrierType());
        processStep.setPreparation(new CarrierType());
        processStepValidator.validate(processStep);
    }

    /**
     * Test Validator with processStep with null parameters
     */
    @Test(expected = ValidationException.class)
    public void testProcessStepNullParameter() throws ValidationException {
        ProcessStep processStep = new ProcessStep();
        processStep.setName("PC-01");
        processStep.setParameters(null);
        processStep.setStateMachine(new StateMachine());
        processStep.setWorkstation(new Workstation());
        processStep.setOutput(new CarrierType());
        processStep.setPreparation(new CarrierType());
        processStepValidator.validate(processStep);
    }

    /**
     * Test Validator with processStep with null stateMachine
     */
    @Test(expected = ValidationException.class)
    public void testProcessStepNullStateMachine() throws ValidationException {
        ProcessStep processStep = new ProcessStep();
        processStep.setName("PC-01");
        processStep.setParameters(new ArrayList<>());
        processStep.setStateMachine(null);
        processStep.setWorkstation(new Workstation());
        processStep.setOutput(new CarrierType());
        processStep.setPreparation(new CarrierType());
        processStepValidator.validate(processStep);
    }

    /**
     * Test Validator with processStep with null workstation
     */
    @Test(expected = ValidationException.class)
    public void testProcessStepNullWorkstation() throws ValidationException {
        ProcessStep processStep = new ProcessStep();
        processStep.setName("PC-01");
        processStep.setParameters(new ArrayList<>());
        processStep.setStateMachine(new StateMachine());
        processStep.setWorkstation(null);
        processStep.setOutput(new CarrierType());
        processStep.setPreparation(new CarrierType());
        processStepValidator.validate(processStep);
    }

}
