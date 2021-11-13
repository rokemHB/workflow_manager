package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.service.WorkstationService;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.faces.context.FacesContext;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test Class to Test {@link WorkstationController}
 *
 * @author Robin
 * @author Arvid
 * @author Sören
 */
public class WorkstationControllerTest {

    /**
     * Injected instance of WorkstationController
     */
    @InjectMocks
    private WorkstationController controller;

    /**
     * Mocked version of WorkstationService
     */
    @Mock
    private WorkstationService service;

    /**
     * Mocked version of Workstation
     */
    @Mock
    private Workstation workstation;

    /**
     * Mocked version of localeController
     */
    @Mock
    private LocaleController localeController;

    /**
     * Setup method of WorkstationControllerTest class
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests, if getAllTechnologen returns a list of all Technologen.
     *
     * @throws FindByException if finding fails
     */
    @Test
    public void testGetAllTechnologenNormal() throws FindByException {
        when(service.getAllTechnologen(any())).thenReturn(new ArrayList<>());

        assertEquals(new ArrayList<>(), controller.getAllTechnologen());
    }

    /**
     * Tests, if getAllTechnologen fails, when a FindByException occurs.
     *
     * @throws FindByException if finding fails
     */
    @Test
    public void testGetAllTechnologenFail() throws FindByException {
        FacesContext context = ContextMocker.mockFacesContext();
        when(service.getAllTechnologen(any())).thenThrow(FindByException.class);

        controller.getAllTechnologen();
        verify(localeController, times(1)).formatString(any());
    }

    /**
     * Tests, if create calls the service correctly.
     *
     * @throws CreationException if creation fails
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateNormal() throws CreationException, EntityAlreadyExistingException {
        when(service.create(any())).thenReturn(workstation);

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
        FacesContext context = ContextMocker.mockFacesContext();
        doThrow(UpdateException.class).when(service).update(any());

        controller.update();
        verify(localeController, times(2)).formatString(any());
    }
}