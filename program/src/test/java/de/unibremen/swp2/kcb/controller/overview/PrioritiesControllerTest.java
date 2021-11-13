package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.service.PriorityService;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class to test {@link PrioritiesController}
 *
 * @author Robin
 */
public class PrioritiesControllerTest {

    /**
     * Injected instance of assembliesController.
     */
    @InjectMocks
    PrioritiesController prioritiesController;

    /**
     * Mocked version of assemblyService entity.
     */
    @Mock
    PriorityService priorityService;

    /**
     * Mocked version of LocaleController.
     */
    @Mock
    LocaleController localeController;

    /**
     * SetUp Method to inject Mock-Objects.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test init.
     */
    @Test
    public void testInit() {
        prioritiesController.init();
        List<Priority> empty = new ArrayList<>();
        when(priorityService.getAll()).thenReturn(empty);
        verify(priorityService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of PrioritiesController.
     */
    @Test
    public void testRefresh() {
        List<Priority> prios = new ArrayList<>();
        prios.add(new Priority());
        when(priorityService.getAll()).thenReturn(prios);
        prioritiesController.refresh();
        verify(priorityService, times(1)).getAll();
        assertEquals(prioritiesController.getEntities(), prios);
    }

    /**
     * Test refresh fail.
     */
    @Test
    public void testRefreshFail() {
        when(priorityService.getAll()).thenReturn(null);
        prioritiesController.refresh();
        verify(priorityService, times(1)).getAll();
        List<Priority> empty = new ArrayList<>();
        assertEquals(prioritiesController.getEntities(), empty);
    }

    /**
     * Tests the getById() method of assembliesController.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetById() throws InvalidIdException {
        Priority dummy = new Priority();
        dummy.setId("111");
        when(priorityService.getById("111")).thenReturn(dummy);
        assertEquals(prioritiesController.getById("111"), dummy);
        verify(priorityService, times(1)).getById(anyString());
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(priorityService.getById("123")).thenThrow(new InvalidIdException("fail"));
        assertNull(prioritiesController.getById("123"));
        verify(priorityService, times(1)).getById(anyString());
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        List<Priority> list = new ArrayList<>();
        Priority processChain = new Priority();
        list.add(processChain);
        when(priorityService.getAll()).thenReturn(list);
        assertEquals(prioritiesController.getAll(), list);
        verify(priorityService, times(1)).getAll();
    }
}