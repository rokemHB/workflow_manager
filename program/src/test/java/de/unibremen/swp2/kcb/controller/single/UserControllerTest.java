package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.TransportService;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.EntityAlreadyExistingException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.faces.context.FacesContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link UserController}
 *
 * @author Robin
 * @author Arvid
 */
public class UserControllerTest {

    /**
     * Injected instance of controller
     */
    @InjectMocks
    private UserController controller;

    /**
     * Mocked version of transportService
     */
    @Mock
    private TransportService transportService;

    /**
     * Mocked version of service
     */
    @Mock
    private UserService service;

    /**
     * Mocked version of model
     */
    @Mock
    private User model;

    /**
     * Mocked version of localeController
     */
    @Mock
    private LocaleController localeController;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests, if refresh works correctly.
     */
    @Test
    public void testRefresh() {
        controller.refresh();
        //noinspection deprecation
        assertEquals(controller.getAllRoles(), Role.values());
    }

    /**
     * Tests, if reset works correctly.
     */
    @Test
    public void testReset() {
        controller.reset();
        assertTrue(controller.getEntity().getRoles().isEmpty());
    }

    /**
     * Tests, if create calls the service correctly.
     *
     * @throws CreationException if creation fails
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateNormal() throws CreationException, EntityAlreadyExistingException {
        when(service.create(any())).thenReturn(model);
        doNothing().when(transportService).create(any());

        controller.create();
        verify(service, times(1)).create(any());
    }

    /**
     * Tests, if create fails, when CreationException occurs.
     *
     * @throws CreationException if creation fails
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateFail() throws CreationException, EntityAlreadyExistingException {
        FacesContext context = ContextMocker.mockFacesContext();
        when(service.create(any())).thenThrow(CreationException.class);

        controller.create();
        verify(localeController, times(2)).formatString(any());
    }

    /**
     * Tests, if delete calls the service correctly.
     *
     * @throws DeletionException if deletion fails
     */
    @Test
    public void testDeleteNormal() throws DeletionException {
        controller.delete();
        doNothing().when(transportService).create(any());

        verify(service, times(1)).delete(any());
    }

    /**
     * Tests, if delete fails, when DeletionException occurs.
     *
     * @throws DeletionException if deletion fails
     */
    @Test
    public void testDeleteFail() throws DeletionException {
        FacesContext context = ContextMocker.mockFacesContext();
        doThrow(DeletionException.class).when(service).delete(any());
        doNothing().when(transportService).create(any());

        controller.delete();
        verify(localeController, times(2)).formatString(any());
    }

    /**
     * Tests, if update calls the service correctly.
     *
     * @throws UpdateException if updating fails
     */
    @Test
    public void testUpdateNormal() throws UpdateException {
        controller.update();
        doNothing().when(transportService).create(any());

        verify(service, times(1)).update(any());
    }

    /**
     * Tests, if update fails, when UpdateException occurs.
     *
     * @throws UpdateException if updating fails
     */
    @Test
    public void testUpdateFail() throws UpdateException {
        FacesContext context = ContextMocker.mockFacesContext();
        doThrow(UpdateException.class).when(service).update(any());

        controller.update();
        verify(localeController, times(2)).formatString(any());
    }
}