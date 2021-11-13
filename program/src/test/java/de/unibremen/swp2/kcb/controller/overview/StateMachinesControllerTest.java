package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.service.StateMachineService;
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
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link StateMachinesController}
 *
 * @author Robin
 * @author Arvid
 */
public class StateMachinesControllerTest {

    /**
     * Injected instance of StateMachinesController.
     */
    @InjectMocks
    StateMachinesController stateMachinesController;

    /**
     * Mocked version of StateMachineService entity.
     */
    @Mock
    StateMachineService stateMachineService;

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
        stateMachinesController.init();
        List<StateMachine> empty = new ArrayList<>();
        when(stateMachineService.getAll()).thenReturn(empty);
        verify(stateMachineService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of stateMachinesController. Tests whether getAll() gets called
     * in stateMachineService.
     */
    @Test
    public void testRefresh() {
        List<StateMachine> steps = new ArrayList<>();
        steps.add(new StateMachine());
        when(stateMachineService.getAll()).thenReturn(steps);
        stateMachinesController.refresh();
        verify(stateMachineService, times(1)).getAll();
        assertEquals(stateMachinesController.getEntities(), steps);
    }

    /**
     * Tests the getById() method of stateMachinesController.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetById() throws InvalidIdException {
        StateMachine dummy = new StateMachine();
        dummy.setId("111");
        when(stateMachineService.getById("111")).thenReturn(dummy);
        assertEquals(stateMachinesController.getById("111"), dummy);
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(stateMachineService.getById("123")).thenThrow(InvalidIdException.class);
        assertNull(stateMachinesController.getById("123"));
    }

    /**
     * Tests the getByName() Method with a List containing one and a List containing two
     * stateMachines respectively.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByName() throws FindByException {
        StateMachine dummy0 = new StateMachine();
        dummy0.setName("Dummkopf");
        List<StateMachine> nameList = new ArrayList<>();
        nameList.add(dummy0);
        when(stateMachineService.getByName("Dummkopf")).thenReturn(nameList);
        assertEquals(stateMachinesController.getByName("Dummkopf"), nameList);
    }

    /**
     * Tests the getByName() Method in case name was not found.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByNameFail() throws FindByException {
        List<StateMachine> empty = new ArrayList<>();
        when(stateMachineService.getByName("test")).thenThrow(FindByException.class);
        assertEquals(stateMachinesController.getByName("test"), empty);
    }

}