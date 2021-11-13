package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.service.StateService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
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
 * Test Class to Test {@link StatesController}
 *
 * @author Robin
 */
public class StatesControllerTest {

    /**
     * Injected instance of assembliesController.
     */
    @InjectMocks
    StatesController statesController;

    /**
     * Mocked version of assemblyService entity.
     */
    @Mock
    StateService stateService;

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
        statesController.init();
        List<State> empty = new ArrayList<>();
        when(stateService.getAll()).thenReturn(empty);
        verify(stateService, times(1)).getAll();
    }

    /**
     * Test refresh.
     */
    @Test
    public void testRefresh() {
        List<State> state = new ArrayList<>();
        state.add(new State());
        when(stateService.getAll()).thenReturn(state);
        statesController.refresh();
        verify(stateService, times(1)).getAll();
        assertEquals(statesController.getEntities(), state);
    }

    /**
     * Test refresh fail.
     */
    @Test
    public void testRefreshFail() {
        when(stateService.getAll()).thenReturn(null);
        statesController.refresh();
        verify(stateService, times(1)).getAll();
        List<State> empty = new ArrayList<>();
        assertEquals(statesController.getEntities(), empty);
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        State dummy = new State();
        dummy.setId("111");
        when(stateService.getById("111")).thenReturn(dummy);
        assertEquals(statesController.getById("111"), dummy);
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(stateService.getById("123")).thenThrow(new InvalidIdException("fail"));
        assertNull(statesController.getById("123"));
    }

    /**
     * Test get by name.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByName() throws FindByException {
        State dummy = new State();
        dummy.setName("name");
        List<State> list = new ArrayList<>();
        list.add(dummy);
        when(stateService.getByName("name")).thenReturn(list);
        assertEquals(statesController.getByName("name"), list);
        verify(stateService, times(1)).getByName(anyString());
    }

    /**
     * Test get by name fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByNameFail() throws FindByException {
        when(stateService.getByName(anyString())).thenThrow(FindByException.class);
        List<State> empty = new ArrayList<>();
        assertEquals(statesController.getByName(anyString()), empty);
    }

    /**
     * Test get by blocking.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByBlocking() throws FindByException {
        State dummy = new State();
        dummy.setBlocking(true);
        List<State> list = new ArrayList<>();
        list.add(dummy);
        when(stateService.getByBlocking(true)).thenReturn(list);
        assertEquals(statesController.getByBlocking(true), list);
        verify(stateService, times(1)).getByBlocking(true);
    }

    /**
     * Test get by blocking fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByBlockingFail() throws FindByException {
        when(stateService.getByBlocking(true)).thenThrow(FindByException.class);
        List<State> empty = new ArrayList<>();
        assertEquals(statesController.getByBlocking(true), empty);
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        State dummy = new State();
        List<State> list = new ArrayList<>();
        list.add(dummy);
        when(stateService.getAll()).thenReturn(list);
        assertEquals(statesController.getAll(), list);
        verify(stateService, times(1)).getAll();
    }

}