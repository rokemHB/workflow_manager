package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.GlobalConfig;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.GlobalConfigRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
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
 * Test Class to Test {@link GlobalConfigService}
 *
 * @author Robin
 * @author Arvid
 */
public class GlobalConfigServiceTest {

    /**
     * Injected instance of GlobalConfigService
     */
    @InjectMocks
    private GlobalConfigService service;

    /**
     * Mocked version of GlobalConfigRepository
     */
    @Mock
    private GlobalConfigRepository repository;

    /**
     * Mocked version of UserService
     */
    @Mock
    private UserService userService;

    /**
     * Mocked version of User
     */
    @Mock
    private User user;

    /**
     * Mocked version of the configuration entity
     */
    @Mock
    private GlobalConfig globalConfig;

    /**
     * Setup method for GlobalConfigServiceTest
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Setup method for CUD methods
     */
    private void setUpCreateUpdateDelete() {
        when(repository.findBy(globalConfig.getKey())).thenReturn(globalConfig);
        when(repository.save(globalConfig)).thenReturn(globalConfig);
    }

    /**
     * Setup method of Executing User
     */
    private void setUpExecutingUser() {
        when(userService.getExecutingUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("TestUser");
    }

    /**
     * Test, if updating a non existing configuration throws an UpdateException.
     *
     * @throws UpdateException If updating of the configuration fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateGlobalConfigFails() throws UpdateException {
        when(userService.getExecutingUser()).thenReturn(user);
        when(repository.findBy(globalConfig.getKey())).thenReturn(null);

        service.update(globalConfig);
    }

    /**
     * Tests, if updating of an configuration, without an executing user, throws a UpdateException.
     *
     * @throws ValidationException If validation of the configuration fails.
     * @throws UpdateException If updating of the configuration fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateMissingUser() throws ValidationException, UpdateException {
        when(userService.getExecutingUser()).thenReturn(null);

        service.update(globalConfig);
    }

    /**
     * Tests, if deleting of a configuration, without an executing user, throws a
     * DeletionException.
     *
     * @throws ValidationException If validation of the configuration fails.
     * @throws DeletionException If deleting of the configuration fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteMissingUser() throws ValidationException, DeletionException {
        when(userService.getExecutingUser()).thenReturn(null);

        service.delete(globalConfig);
    }

    /**
     * Tests, if creation of an configuration returns the configuration.
     *
     * @throws ValidationException If validation of the configuration fails.
     * @throws CreationException If creation of the configuration fails.
     */
    @Test
    public void testCreateGlobalConfig() throws ValidationException, CreationException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        assertEquals(globalConfig, service.create(globalConfig));
    }

    /**
     * Tests, if updating of an configuration returns the new configuration.
     *
     * @throws ValidationException If validation of the configuration fails.
     * @throws UpdateException If updating of the configuration fails.
     */
    @Test
    public void testUpdateGlobalConfig() throws ValidationException, UpdateException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        assertEquals(globalConfig, service.update(globalConfig));
    }

    /**
     * Tests, if deleting an configuration works without exceptions.
     *
     * @throws ValidationException If validation of the configuration fails.
     * @throws DeletionException If deletion of the configuration fails.
     */
    @Test
    public void testDeleteGlobalConfig() throws ValidationException, DeletionException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.delete(globalConfig);
        verify(repository, times(1)).attachAndRemove(any());
    }

    /**
     * Tests, if getByKey returns a configurations referring to a key.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test
    public void testGetByKey() throws FindByException {
        List<GlobalConfig> globalConfigCollection = new ArrayList<>();
        globalConfigCollection.add(globalConfig);
        when(repository.findByKey("TestKey")).thenReturn(globalConfigCollection);

        assertEquals(globalConfig, service.getByKey("TestKey"));
    }

    /**
     * Tests, if getByKey throws an exception if key is null.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test(expected = FindByException.class)
    public void testGetByKeyFails() throws FindByException {
        service.getByKey(null);
    }

    /**
     * Tests, if getAll returns a list of all configs.
     */
    @Test
    public void testGetAllNormal() {
        List<GlobalConfig> globalConfigCollection = new ArrayList<>();
        globalConfigCollection.add(globalConfig);
        when(repository.findAll()).thenReturn(globalConfigCollection);

        assertEquals(globalConfigCollection, service.getAll());
    }
}