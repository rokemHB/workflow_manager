package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.persistence.parameter.ParameterRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.ParameterValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link ParameterService}
 *
 * @author Robin
 * @author Arvid
 */
public class ParameterServiceTest {

    /**
     * Injected instance of service
     */
    @InjectMocks
    private ParameterService service;

    /**
     * Mocked version of validator
     */
    @Mock
    private ParameterValidator validator;

    /**
     * Mocked version repository
     */
    @Mock
    private ParameterRepository repository;

    /**
     * Mocked version of userService
     */
    @Mock
    private UserService userService;

    /**
     * Mocked version of User
     */
    @Mock
    private User user;

    /**
     * Mocked version of the parameter entity
     */
    @Mock
    private Parameter parameter;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * setup method for CUD methods
     * @throws ValidationException when validation fails
     */
    private void setUpCreateUpdateDelete() throws ValidationException {
        when(validator.validate(parameter)).thenReturn(true);
        when(repository.findBy(parameter.getId())).thenReturn(parameter);
        when(repository.save(parameter)).thenReturn(parameter);
    }

    /**
     * sets up an existing user
     */
    private void setUpExecutingUser() {
        when(userService.getExecutingUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("TestUser");
    }

    /**
     * Tests, if creation of a null object throws a CreationException.
     *
     * @throws ValidationException If validation of the parameter fails.
     * @throws CreationException If creation of the parameter fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFails() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.create(null);
        verify(validator, times(1)).validate(null);
    }

    /**
     * Tests, if updating of a null object throws a UpdateException.
     *
     * @throws ValidationException If validation of the parameter fails.
     * @throws UpdateException If updating of the parameter fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationFails() throws ValidationException, UpdateException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.update(null);
        verify(validator, times(1)).validate(null);
    }

    /**
     * Test, if updating a non existing parameter throws an UpdateException.
     *
     * @throws ValidationException If validation of the parameter fails.
     * @throws UpdateException If updating of the parameter fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateParameterFails() throws ValidationException, UpdateException {
        when(validator.validate(parameter)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(repository.findBy(parameter.getId())).thenReturn(null);

        service.update(parameter);
    }

    /**
     * Tests, if deleting an non existing parameter throws a DeletionException.
     *
     * @throws ValidationException If validation of the parameter fails.
     * @throws DeletionException If deletion of the parameter fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteParameterFails() throws ValidationException, DeletionException {
        setUpExecutingUser();

        when(validator.validate(parameter)).thenReturn(true);
        when(repository.findBy(parameter.getId())).thenReturn(null);

        service.delete(parameter);
    }

    /**
     * Tests, if updating of an parameter, without an executing user, throws a UpdateException.
     *
     * @throws ValidationException If validation of the parameter fails.
     * @throws UpdateException If updating of the parameter fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateMissingUser() throws ValidationException, UpdateException {
        when(validator.validate(parameter)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(null);

        service.update(parameter);
    }

    /**
     * Tests, if deleting of a parameter, without an executing user, throws a DeletionException.
     *
     * @throws DeletionException If deleting of the parameter fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteMissingUser() throws DeletionException {
        when(userService.getExecutingUser()).thenReturn(null);

        service.delete(parameter);
    }

    /**
     * Tests, if creation of an parameter returns the parameter.
     *
     * @throws ValidationException If validation of the parameter fails.
     * @throws CreationException If creation of the parameter fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateParameter() throws ValidationException, CreationException, EntityAlreadyExistingException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        assertEquals(parameter, service.create(parameter));
    }

    /**
     * Tests, if updating of an parameter returns the new parameter.
     *
     * @throws ValidationException If validation of the parameter fails.
     * @throws UpdateException If updating of the parameter fails.
     */
    @Test
    public void testUpdateParameter() throws ValidationException, UpdateException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.update(parameter);
        verify(repository, times(1)).saveAndFlushAndRefresh(parameter);
    }

    /**
     * Tests, if deleting an parameter works without exceptions.
     *
     * @throws ValidationException If validation of the parameter fails.
     * @throws DeletionException If deletion of the parameter fails.
     */
    @Test
    public void testDeleteParameter() throws ValidationException, DeletionException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.delete(parameter);
    }

    /**
     * Tests, if getByField returns a list of all parameters referring to a field.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test
    public void testGetByField() throws FindByException {
        List<Parameter> parameterCollection = new ArrayList<>();
        parameterCollection.add(parameter);
        when(repository.findByField("TestField")).thenReturn(parameterCollection);

        assertEquals(parameterCollection, service.getByField("TestField"));
    }

    /**
     * Tests, if getByField throws an exception if field is null.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test(expected = FindByException.class)
    public void testGetByFieldFails() throws FindByException {
        service.getByField(null);
    }
}