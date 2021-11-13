package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.persistence.parameter.ValueRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import de.unibremen.swp2.kcb.validator.backend.ValueValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link ValueService}
 *
 * @author Marc
 */
public class ValueServiceTest {

    /**
     * Injected instance of ValueService
     */
    @InjectMocks
    private ValueService valueService;

    /**
     * Mocked version of ValueRepository
     */
    @Mock
    private ValueRepository valueRepository;

    /**
     * Mocked version of ValueValidator
     */
    @Mock
    private ValueValidator valueValidator;

    /**
     * Mocked version of UserService
     */
    @Mock
    private UserService userService;

    /**
     * Mocked version of Value
     */
    @Mock
    private Value valueMock;

    /**
     * Mocked version of User
     */
    @Mock
    private User user;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test create success.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test
    public void testCreateSuccess() throws ValidationException, CreationException {
        Value value = new Value();

        when(valueValidator.validate(any())).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.save(value)).thenReturn(value);

        valueService.create(value);

        verify(valueValidator, times(1)).validate(value);
        verify(valueRepository, times(1)).save(value);
    }

    /**
     * Test create validation false.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFalse() throws ValidationException, CreationException {
        Value value = new Value();

        when(valueValidator.validate(any())).thenReturn(false);
        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.save(value)).thenReturn(value);

        valueService.create(value);

        verify(valueValidator, times(1)).validate(value);
        verify(valueRepository, times(1)).save(value);
    }

    /**
     * Test create validation fail.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFail() throws ValidationException, CreationException {
        Value value = new Value();

        when(valueValidator.validate(any())).thenThrow(ValidationException.class);
        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.save(value)).thenReturn(value);

        valueService.create(value);

        verify(valueValidator, times(1)).validate(value);
        verify(valueRepository, times(1)).save(value);
    }

    /**
     * Test create save fail.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateSaveFail() throws ValidationException, CreationException {
        Value value = new Value();

        when(valueValidator.validate(any())).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.save(value)).thenThrow(PersistenceException.class);

        valueService.create(value);

        verify(valueValidator, times(1)).validate(value);
        verify(valueRepository, times(1)).save(value);
    }

    /**
     * Test update success.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test
    public void testUpdateSuccess() throws ValidationException, UpdateException {
        Value value = new Value();

        when(valueValidator.validate(any())).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.saveAndFlushAndRefresh(value)).thenReturn(value);
        when(valueRepository.findBy(anyString())).thenReturn(value);

        valueService.update(value);

        verify(valueValidator, times(1)).validate(value);
        verify(valueRepository, times(1)).saveAndFlushAndRefresh(value);
    }

    /**
     * Test update validation fail.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationFail() throws ValidationException, UpdateException {
        Value value = new Value();

        when(valueValidator.validate(any())).thenThrow(ValidationException.class);
        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.saveAndFlushAndRefresh(value)).thenReturn(value);
        when(valueRepository.findBy(anyString())).thenReturn(value);

        valueService.update(value);

        verify(valueValidator, times(1)).validate(value);
        verify(valueRepository, times(1)).saveAndFlushAndRefresh(value);
    }

    /**
     * Test update validation false.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationFalse() throws ValidationException, UpdateException {
        Value value = new Value();

        when(valueValidator.validate(any())).thenReturn(false);
        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.saveAndFlushAndRefresh(value)).thenReturn(value);
        when(valueRepository.findBy(anyString())).thenReturn(value);

        valueService.update(value);

        verify(valueValidator, times(1)).validate(value);
        verify(valueRepository, times(1)).saveAndFlushAndRefresh(value);
    }

    /**
     * Test update find by null.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateFindByNull() throws ValidationException, UpdateException {
        Value value = new Value();

        when(valueValidator.validate(any())).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.saveAndFlushAndRefresh(value)).thenReturn(value);
        when(valueRepository.findBy(anyString())).thenReturn(null);

        valueService.update(value);

        verify(valueValidator, times(1)).validate(value);
        verify(valueRepository, times(1)).saveAndFlushAndRefresh(value);
    }

    /**
     * Test update save fail.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateSaveFail() throws ValidationException, UpdateException {
        Value value = new Value();

        when(valueValidator.validate(any())).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.saveAndFlushAndRefresh(value)).thenThrow(PersistenceException.class);
        when(valueRepository.findBy(anyString())).thenReturn(value);

        valueService.update(value);

        verify(valueValidator, times(1)).validate(value);
        verify(valueRepository, times(1)).saveAndFlushAndRefresh(value);
    }

    /**
     * Test delete success.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDeleteSuccess() throws DeletionException {
        Value value = new Value();

        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.findBy(anyString())).thenReturn(value);

        valueService.delete(value);

        verify(valueRepository, times(1)).attachAndRemove(value);
    }

    /**
     * Test delete find by null.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteFindByNull() throws DeletionException {
        Value value = new Value();

        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.findBy(anyString())).thenReturn(value);
        doThrow(PersistenceException.class).when(valueRepository).attachAndRemove(value);

        valueService.delete(value);

        verify(valueRepository, times(1)).attachAndRemove(value);
    }

    /**
     * Test delete remove fail.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteRemoveFail() throws DeletionException {
        Value value = new Value();

        when(userService.getExecutingUser()).thenReturn(user);
        when(valueRepository.findBy(anyString())).thenReturn(null);

        valueService.delete(value);

        verify(valueRepository, times(1)).attachAndRemove(value);
    }

    /**
     * Test get by id success.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIDSuccess() throws InvalidIdException {

        when(valueMock.getId()).thenReturn("12345");
        when(valueRepository.findBy(anyString())).thenReturn(valueMock);
        when(userService.getExecutingUser()).thenReturn(user);

        assertEquals(valueMock, valueService.getById("12345"));
        verify(valueRepository, times(1)).findBy("12345");
    }

    /**
     * Test get by id find by fail.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIDFindByFail() throws InvalidIdException {

        when(valueMock.getId()).thenReturn("12345");
        when(valueRepository.findBy(anyString())).thenThrow(PersistenceException.class);
        when(userService.getExecutingUser()).thenReturn(user);

        assertEquals(valueMock, valueService.getById("12345"));
        verify(valueRepository, times(1)).findBy("12345");
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        List<Value> valList = new ArrayList<>();
        when(valueRepository.findAll()).thenReturn(valList);

        assertEquals(valList, valueService.getAll());
    }
}