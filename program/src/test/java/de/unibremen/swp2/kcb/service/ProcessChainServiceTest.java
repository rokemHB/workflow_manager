package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.persistence.ProcessChainRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.ProcessChainValidator;
import de.unibremen.swp2.kcb.validator.backend.ProcessStepValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import de.unibremen.swp2.kcb.validator.backend.ValidationNullPointerException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link ProcessChainService}
 *
 * @author Marc
 * @author Robin
 * @author Arvid
 */
public class ProcessChainServiceTest {

    /**
     * Injected instance of service
     */
    @InjectMocks
    private ProcessChainService processChainService;

    /**
     * Mocked version of processChainValidator
     */
    @Mock
    private ProcessChainValidator processChainValidator;

    /**
     * Mocked version of processStepValidator
     */
    @Mock
    private ProcessStepValidator processStepValidator;

    /**
     * Mocked version of repository
     */
    @Mock
    private ProcessChainRepository processChainRepository;

    /**
     * ProcessStepList Attribute
     */
    private List<ProcessStep> stepList;

    /**
     * ProcessChain attribute
     */
    private ProcessChain p;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stepList = new ArrayList<>();
        p = new ProcessChain();
        setUpChain();
    }

    /**
     * Setup Method for ProcessChains
     */
    private void setUpChain() {
        p.setId("123");
        p.setName("MyChain");
        p.setChain(stepList);
    }

    /**
     * Test create validation name null.
     *
     * @throws CreationException the creation exception
     * @throws ValidationException the validation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationNameNull() throws CreationException, ValidationException, EntityAlreadyExistingException {
        p.setName(null);
        when(processChainValidator.validate(any())).thenThrow(ValidationException.class);
        processChainService.create(p);
    }

    /**
     * Test create validation id null.
     *
     * @throws CreationException the creation exception
     * @throws ValidationException the validation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationIdNull() throws CreationException, ValidationException, EntityAlreadyExistingException {
        p.setId(null);
        when(processChainValidator.validate(any())).thenThrow(ValidationException.class);
        processChainService.create(p);
    }

    /**
     * Test create validation chain null.
     *
     * @throws CreationException the creation exception
     * @throws ValidationException the validation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationChainNull() throws CreationException, ValidationException, EntityAlreadyExistingException {
        p.setChain(null);
        when(processChainValidator.validate(any())).thenThrow(ValidationException.class);
        processChainService.create(p);
    }

    /**
     * Test update validation id null.
     *
     * @throws UpdateException the update exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationIdNull() throws UpdateException, ValidationException {
        p.setId(null);
        when(processChainValidator.validate(any())).thenThrow(ValidationException.class);
        processChainService.update(p);
    }

    /**
     * Test update validation name null.
     *
     * @throws UpdateException the update exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationNameNull() throws UpdateException, ValidationException {
        p.setName(null);
        when(processChainValidator.validate(any())).thenThrow(ValidationException.class);
        processChainService.update(p);
    }

    /**
     * Test update validation chain null.
     *
     * @throws UpdateException the update exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationChainNull() throws UpdateException, ValidationException {
        p.setChain(null);
        when(processChainValidator.validate(any())).thenThrow(ValidationException.class);
        processChainService.update(p);
    }

    /**
     * Test get by id invalid empty.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdInvalidEmpty() throws InvalidIdException {
        processChainService.getById("");
    }

    /**
     * Test get by id invalid null.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdInvalidNull() throws InvalidIdException {
        processChainService.getById(null);
    }

    /**
     * Test get by name invalid empty.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByNameInvalidEmpty() throws FindByException {
        processChainService.getByName(p.getName());
    }

    /**
     * Test get by name invalid null.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByNameInvalidNull() throws FindByException {
        processChainService.getByName(null);
    }

    /**
     * Test get by name not found.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByNameNotFound() throws FindByException {
        when(processChainRepository.findByName(p.getName())).thenReturn(p);
        processChainService.getByName("notFound");
    }

    /**
     * Test get by name successful.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByNameSuccessful() throws FindByException {
        when(processChainRepository.findByName(p.getName())).thenReturn(p);
        assertEquals(processChainService.getByName(p.getName()), p);
    }

    /**
     * Test get duration with zero.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetDurationWithZero() throws FindByException {
        assertEquals(processChainService.getDuration(p), 0);
    }

    /**
     * Test get duration chain null.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetDurationChainNull() throws FindByException {
        p.setChain(null);
        processChainService.getDuration(p);
    }

    /**
     * Test get duration validate chain fail.
     *
     * @throws FindByException the find by exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = FindByException.class)
    public void testGetDurationValidateChainFail() throws FindByException, ValidationException {
        ProcessStep ps1 = new ProcessStep();
        ProcessStep ps2 = new ProcessStep();
        ProcessStep ps3 = new ProcessStep();
        ps1.setEstDuration(10);
        ps2.setEstDuration(50);
        ps3.setEstDuration(20);
        stepList.add(ps1);
        stepList.add(ps2);
        stepList.add(ps3);
        when(processChainValidator.validate(p)).thenThrow(ValidationException.class);

        processChainService.getDuration(p);
    }

    /**
     * Test get duration successful.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetDurationSuccessful() throws FindByException {
        ProcessStep ps1 = new ProcessStep();
        ProcessStep ps2 = new ProcessStep();
        ProcessStep ps3 = new ProcessStep();
        ps1.setEstDuration(10);
        ps2.setEstDuration(50);
        ps3.setEstDuration(20);
        stepList.add(ps1);
        stepList.add(ps2);
        stepList.add(ps3);

        assertEquals(processChainService.getDuration(p), 80);
    }

    /**
     * Test get by process step validate step fail.
     *
     * @throws ValidationException the validation exception
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByProcessStepValidateStepFail() throws ValidationException, FindByException {
        ProcessStep ps1 = new ProcessStep();
        ps1.setName(null);
        stepList.add(ps1);
        when(processStepValidator.validate(ps1)).thenThrow(ValidationNullPointerException.class);
        processChainService.getByProcessStep(ps1);
    }

    /**
     * Test get by process step not in chain.
     *
     * @throws FindByException the find by exception
     * @throws ValidationNullPointerException the validation null pointer exception
     */
    @Test
    public void testGetByProcessStepNotInChain() throws FindByException, ValidationNullPointerException {
        ProcessStep ps1 = new ProcessStep();
        List<ProcessChain> chainList = new ArrayList<>();
        chainList.add(p);

        when(processChainRepository.findAll()).thenReturn(chainList);
        assertEquals(processChainService.getByProcessStep(ps1).size(), 0);
        verify(processStepValidator, times(1)).validate(ps1);
    }

    /**
     * Test get by process step successful.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByProcessStepSuccessful() throws FindByException {
        ProcessStep ps1 = new ProcessStep();
        ProcessStep ps2 = new ProcessStep();
        ProcessStep ps3 = new ProcessStep();
        stepList.add(ps1);
        stepList.add(ps2);
        stepList.add(ps3);
        List<ProcessChain> chainList = new ArrayList<>();
        chainList.add(p);

        when(processChainRepository.findAll()).thenReturn(chainList);
        assertEquals(processChainService.getByProcessStep(ps1).size(), 1);

        assertEquals(processChainService.getByProcessStep(ps1), chainList);
        assertEquals(processChainService.getByProcessStep(ps2), chainList);
        assertEquals(processChainService.getByProcessStep(ps3), chainList);
    }
}