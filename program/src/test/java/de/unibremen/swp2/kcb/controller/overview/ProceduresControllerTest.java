package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.service.ProcedureService;
import de.unibremen.swp2.kcb.service.serviceExceptions.ExecutionException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link ProceduresController}
 *
 * @author Robin
 * @author Arvid
 */
public class ProceduresControllerTest {

    /**
     * Injected instance of ProcessStepsController.
     */
    @InjectMocks
    ProceduresController proceduresController;

    /**
     * Mocked version of processStepService entity.
     */
    @Mock
    ProcedureService procedureService;

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
        proceduresController.init();
        List<Procedure> empty = new ArrayList<>();
        when(procedureService.getAll()).thenReturn(empty);
        verify(procedureService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of proceduresController. Tests whether getAll() gets called in
     * procedureService.
     */
    @Test
    public void testRefresh() {
        List<Procedure> steps = new ArrayList<>();
        steps.add(new Procedure());
        when(procedureService.getAll()).thenReturn(steps);
        proceduresController.refresh();
        verify(procedureService, times(1)).getAll();
        assertEquals(proceduresController.getEntities(), steps);
    }

    /**
     * Tests the getById() method of proceduresController.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetById() throws InvalidIdException {
        Procedure dummy = new Procedure();
        dummy.setId("111");
        when(procedureService.getById("111")).thenReturn(dummy);
        assertEquals(proceduresController.getById("111"), dummy);
        verify(procedureService, times(1)).getById(anyString());
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(procedureService.getById(anyString())).thenThrow(new InvalidIdException("fail"));
        assertNull(proceduresController.getById("123"));
    }

    /**
     * Test get by workstation.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByWorkstation() throws FindByException {
        Procedure dummy = new Procedure();
        Workstation ws = new Workstation();
        List<Procedure> res = new ArrayList<>();
        res.add(dummy);
        when(procedureService.getByWorkstation(ws)).thenReturn(res);
        assertEquals(proceduresController.getByWorkstation(ws), res);
        verify(procedureService, times(1)).getByWorkstation(ws);
    }

    /**
     * Test get by workstataion fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByWorkstataionFail() throws FindByException {
        Workstation ws = new Workstation();
        List<Procedure> empty = new ArrayList<>();
        when(procedureService.getByWorkstation(ws)).thenThrow(new FindByException("fail"));
        assertEquals(proceduresController.getByWorkstation(ws), empty);
        verify(procedureService, times(1)).getByWorkstation(ws);
    }

    /**
     * Test get collectable.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetCollectable() throws FindByException {
        List<Procedure> res = new ArrayList<>();
        Procedure procedure = new Procedure();
        res.add(procedure);
        when(procedureService.getCollectable()).thenReturn(res);
        assertEquals(proceduresController.getCollectable(), res);
        verify(procedureService, times(1)).getCollectable();
    }

    /**
     * Test get collectable fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetCollectableFail() throws FindByException {
        List<Procedure> empty = new ArrayList<>();
        assertEquals(proceduresController.getCollectable(), empty);
        verify(procedureService, times(1)).getCollectable();
    }

    /**
     * Test do exec.
     *
     * @throws ExecutionException the execution exception
     */
    @Test
    public void testDoExec() throws ExecutionException {
        StateExec stateExec = new StateExec();
        //when(procedureService.doExec(any())).thenReturn(stateExec);
        Procedure procedure = new Procedure();
        proceduresController.doExec(procedure);
        verify(procedureService, times(1)).doExec(any());
    }

    /**
     * Test do exec fail.
     *
     * @throws ExecutionException the execution exception
     */
    @Test
    public void testDoExecFail() throws ExecutionException {
        Procedure procedure = new Procedure();
        doThrow(new ExecutionException("fail")).when(procedureService).doExec(any());
        //when(procedureService.doExec(procedure)).thenThrow(new ExecutionException("fail"));
        proceduresController.doExec(procedure);
        verify(procedureService, times(1)).doExec(any());
    }

    /**
     * Test get by value.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByValue() throws FindByException {
        Value value = new Value();
        Procedure procedure = new Procedure();
        List<Procedure> res = new ArrayList<>();
        res.add(procedure);
        when(procedureService.getByValue(value)).thenReturn(res);
        assertEquals(proceduresController.getByValue(value), res);
        verify(procedureService, times(1)).getByValue(value);
    }

    /**
     * Test get by value fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByValueFail() throws FindByException {
        List<Procedure> empty = new ArrayList<>();
        Value value = new Value();
        when(procedureService.getByValue(value)).thenThrow(new FindByException("fail"));
        assertEquals(proceduresController.getByValue(value), empty);
        verify(procedureService, times(1)).getByValue(value);
    }

    /**
     * Test get by process step.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByProcessStep() throws FindByException {
        Procedure procedure = new Procedure();
        ProcessStep ps = new ProcessStep();
        List<Procedure> list = new ArrayList<>();
        list.add(procedure);
        when(procedureService.getByProcessStep(ps)).thenReturn(list);
        assertEquals(proceduresController.getByProcessStep(ps), list);
        verify(procedureService, times(1)).getByProcessStep(ps);
    }

    /**
     * Test get by process step fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByProcessStepFail() throws FindByException {
        List<Procedure> empty = new ArrayList<>();
        ProcessStep processStep = new ProcessStep();
        when(procedureService.getByProcessStep(processStep)).thenThrow(new FindByException("fail"));
        assertEquals(proceduresController.getByProcessStep(processStep), empty);
    }

    /**
     * Test get by state history.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByStateHistory() throws FindByException {
        StateHistory stateHistory = new StateHistory();
        List<Procedure> list = new ArrayList<>();
        Procedure procedure = new Procedure();
        list.add(procedure);
        when(procedureService.getByStateHistory(stateHistory)).thenReturn(list);
        assertEquals(proceduresController.getByStateHistory(stateHistory), list);
        verify(procedureService, times(1)).getByStateHistory(stateHistory);
    }

    /**
     * Test get by state history fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByStateHistoryFail() throws FindByException {
        List<Procedure> empty = new ArrayList<>();
        StateHistory stateHistory = new StateHistory();
        when(procedureService.getByStateHistory(stateHistory)).thenThrow(new FindByException("fail"));
        assertEquals(proceduresController.getByStateHistory(stateHistory), empty);
    }

    /**
     * Test get by state.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByState() throws FindByException {
        State state = new State();
        List<Procedure> list = new ArrayList<>();
        when(procedureService.getByState(state)).thenReturn(list);
        assertEquals(proceduresController.getByState(state), list);
        verify(procedureService, times(1)).getByState(state);
    }

    /**
     * Test get by state fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByStateFail() throws FindByException {
        List<Procedure> empty = new ArrayList<>();
        State state = new State();
        when(procedureService.getByState(state)).thenThrow(new FindByException("fail"));
        assertEquals(proceduresController.getByState(state), empty);
    }
}