package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.service.PriorityService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.EntityAlreadyExistingException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test Class to Test {@link PriorityController}
 *
 * @author Robin
 */
public class PriorityControllerTest {

    /**
     * Injected priorityController instance
     */
    @InjectMocks
    private PriorityController priorityController;

    /**
     * Mocked version of PriorityService
     */
    @Mock
    private PriorityService priorityService;

    /**
     * Mocked version of Priority
     */
    @Mock
    private Priority priority;

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
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateNormal() throws CreationException, EntityAlreadyExistingException {
        when(priorityService.create(any())).thenReturn(priority);
        priorityController.create();
        verify(priorityService, times(1)).create(any());
    }

    /**
     * Tests, if create fails, when CreationException occurs.
     *
     * @throws CreationException if creation fails
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateFail() throws CreationException, EntityAlreadyExistingException {
        when(priorityService.create(any())).thenThrow(CreationException.class);
        priorityController.create();
        verify(localeController, times(2)).formatString(any());
    }

    /**
     * Tests, if delete calls the service correctly.
     *
     * @throws DeletionException if deletion fails
     */
    @Test
    public void testDeleteNormal() throws DeletionException {
        priorityController.delete();
        verify(priorityService, times(1)).delete(any());
    }

    /**
     * Tests, if delete fails, when DeletionException occurs.
     *
     * @throws DeletionException if deletion fails
     */
    @Test
    public void testDeleteFail() throws DeletionException {
        doThrow(DeletionException.class).when(priorityService).delete(any());
        priorityController.delete();
        verify(localeController, times(2)).formatString(any());
    }

    /**
     * Tests, if update calls the service correctly.
     *
     * @throws UpdateException if updating fails
     */
    @Test
    public void testUpdateNormal() throws UpdateException {
        priorityController.update();
        verify(priorityService, times(1)).update(any());
    }

    /**
     * Tests, if update fails, when UpdateException occurs.
     *
     * @throws UpdateException if updating fails
     */
    @Test
    public void testUpdateFail() throws UpdateException {
        doThrow(UpdateException.class).when(priorityService).update(any());
        priorityController.update();
        verify(localeController, times(2)).formatString(any());
    }

}