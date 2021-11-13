package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.parameter.CardinalValue;
import de.unibremen.swp2.kcb.service.CardinalValueService;
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
 * Test Class to Test {@link CardinalValueController}
 *
 * @author Robin
 * @author Arvid
 */
public class CardinalValueControllerTest {

    /**
     * Injected CardinalValueController instance
     */
    @InjectMocks
    private CardinalValueController controller;

    /**
     * Mocked version of CardinalValueService
     */
    @Mock
    private CardinalValueService service;

    /**
     * Mocked version of CardinalValue
     */
    @Mock
    private CardinalValue model;

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
        //FacesContext context = ContextMocker.mockFacesContext();
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