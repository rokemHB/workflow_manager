package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.service.AssemblyService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.faces.context.FacesContext;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test Class to Test {@link AssemblyController}
 *
 * @author Robin
 * @author Arvid
 */
public class AssemblyControllerTest {

    /**
     * Injected instance of controller
     */
    @InjectMocks
    private AssemblyController controller;

    /**
     * Mocked version of service
     */
    @Mock
    private AssemblyService service;

    /**
     * Mocked version of model
     */
    @Mock
    private Assembly model;

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
     * Tests, if create calls the service correctly.
     *
     * @throws CreationException if creation fails
     */
    @Test
    public void testCreateNormal() throws CreationException {
        when(service.create(any())).thenReturn(model);

        controller.create();
        verify(service, times(1)).create(any());
    }

    /**
     * Tests, if create fails, when CreationException occurs.
     *
     * @throws CreationException if creation fails
     */
    @Test
    public void testCreateFail() throws CreationException {
        when(service.create(any())).thenThrow(CreationException.class);

        controller.create();
        verify(localeController, times(2)).formatString(any());
    }

    /**
     * Tests, if create fails, when CreationException occurs.
     *
     * @throws CreationException if creation fails
     */
    @Test
    public void testCreateFail2() throws CreationException {
        controller.entity = new Assembly();
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
        verify(service, times(1)).delete(any());
    }

    /**
     * Tests, if delete fails, when DeletionException occurs.
     *
     * @throws DeletionException if deletion fails
     */
    @Test
    public void testDeleteFail() throws DeletionException {
        doThrow(DeletionException.class).when(service).delete(any());

        controller.delete();
        verify(localeController, times(2)).formatString(any());
    }

    /**
     * Tests, if delete fails, when DeletionException occurs.
     *
     * @throws DeletionException if deletion fails
     */
    @Test
    public void testDeleteFail2() throws DeletionException {
        controller.entity = new Assembly();
        doThrow(DeletionException.class).when(service).delete(any());

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
        verify(service, times(1)).update(any());
    }

    /**
     * Tests, if update fails, when UpdateException occurs.
     *
     * @throws UpdateException if updating fails
     */
    @Test
    public void testUpdateFail() throws UpdateException {
        doThrow(UpdateException.class).when(service).update(any());

        controller.update();
        verify(localeController, times(2)).formatString(any());

    }

    /**
     * Tests, if update fails, when UpdateException occurs.
     *
     * @throws UpdateException if updating fails
     */
    @Test
    public void testUpdateFail2() throws UpdateException {
        controller.entity = new Assembly();
        doThrow(UpdateException.class).when(service).update(any());

        controller.update();
        verify(localeController, times(2)).formatString(any());
    }
}