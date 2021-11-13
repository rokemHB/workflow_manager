package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.ProcessStepRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.ProcessStepValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.PersistenceException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link ProcessStepService}
 *
 * @author Robin
 * @author Arvid
 */
public class ProcessStepServiceTest {

    /**
     * Injected instance of processStepService
     */
    @InjectMocks
    private ProcessStepService processStepService;

    /**
     * Mocked version of processStepRepository
     */
    @Mock
    private ProcessStepRepository processStepRepository;

    /**
     * Mocked version of processStepValidator
     */
    @Mock
    private ProcessStepValidator processStepValidator;

    /**
     * Mocked version of userService
     */
    @Mock
    private UserService userService;

    /**
     * Sets up tests.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test create.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreate() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(processStepValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        ProcessStep processStep = new ProcessStep();
        processStepService.create(processStep);
        verify(processStepRepository, times(1)).save(processStep);
    }

    /**
     * Test invalid create.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationException() throws ValidationException, CreationException, EntityAlreadyExistingException {
        //when(processStepValidator.validate(any())).thenThrow(ValidationException.class);
        processStepService.create(null);
    }

    /**
     * Test create invalid.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateInvalid() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(processStepValidator.validate(any())).thenReturn(false);
        processStepService.create(new ProcessStep());
    }

    /**
     * Test create save in repo fail.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateSaveInRepoFail() throws ValidationException, CreationException, EntityAlreadyExistingException {
        ProcessStep processStep = new ProcessStep();
        doThrow(new PersistenceException("create failed")).when(processStepRepository).save(processStep);
        when(processStepValidator.validate(any())).thenReturn(true);
        processStepService.create(processStep);
    }

    /**
     * Test update.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test
    public void testUpdate() throws ValidationException, UpdateException {
        when(processStepValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        ProcessStep processStep = new ProcessStep();
        when(processStepRepository.findBy(any())).thenReturn(processStep);
        processStepService.update(processStep);
        verify(processStepRepository, times(1)).saveAndFlushAndRefresh(processStep);
    }

    /**
     * Test update validation exception.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationException() throws ValidationException, UpdateException {
        processStepService.update(null);
    }

    /**
     * Test update invalid.
     *
     * @throws UpdateException the update exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateInvalid() throws UpdateException, ValidationException {
        when(processStepValidator.validate(any())).thenReturn(false);
        processStepService.update(new ProcessStep());
    }

    /**
     * Test update save in repo fail.
     *
     * @throws UpdateException the update exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateSaveInRepoFail() throws UpdateException, ValidationException {
        ProcessStep processStep = new ProcessStep();
        doThrow(new PersistenceException("create failed")).when(processStepRepository).saveAndFlushAndRefresh(processStep);
        when(processStepValidator.validate(any())).thenReturn(true);
        when(processStepRepository.findBy(any())).thenReturn(new ProcessStep());
        processStepService.update(processStep);
    }

    /**
     * Test update stored value fail.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateStoredValueFail() throws ValidationException, UpdateException {
        when(processStepValidator.validate(any())).thenReturn(true);
        when(processStepRepository.findBy(any())).thenReturn(null);
        processStepService.update(new ProcessStep());
    }

    /**
     * Test delete.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDelete() throws DeletionException {
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        when(processStepRepository.findBy(any())).thenReturn(new ProcessStep());
        doNothing().when(processStepRepository).attachAndRemove(any());
        processStepService.delete(new ProcessStep());
        verify(processStepRepository, times(1)).attachAndRemove(any());
    }

    /**
     * Test delete stored null.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteStoredNull() throws DeletionException {
        when(processStepRepository.findBy(any())).thenReturn(null);
        processStepService.delete(new ProcessStep());
    }

    /**
     * Test delete from repo fail.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteFromRepoFail() throws DeletionException {
        when(processStepRepository.findBy(any())).thenReturn(new ProcessStep());
        doThrow(new PersistenceException("deleteFailed")).when(processStepRepository).attachAndRemove(any());
        processStepService.delete(new ProcessStep());
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        ProcessStep processStep = new ProcessStep();
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        when(processStepRepository.findBy("testId")).thenReturn(processStep);
        assertEquals(processStepService.getById("testId"), processStep);
        verify(processStepRepository, times(1)).findBy(any());
    }

    /**
     * Test get by id invalid id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdInvalidId() throws InvalidIdException {
        processStepService.getById(null);
    }

    /**
     * Test get by id repo fail.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdRepoFail() throws InvalidIdException {
        doThrow(PersistenceException.class).when(processStepRepository).findBy(any());
        processStepService.getById("testId");
    }

}