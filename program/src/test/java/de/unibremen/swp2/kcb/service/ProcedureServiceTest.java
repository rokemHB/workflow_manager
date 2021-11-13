package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.persistence.ProcedureRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link ProcedureService}
 *
 * @author Marc
 * @author Robin
 * @author Arvid
 */
public class ProcedureServiceTest {

    /**
     * Instance of ProcedureService that gets all mocked instances injected.
     */
    @InjectMocks
    private ProcedureService procedureService;

    /**
     * Mocked version of ProcedureValidator.
     */
    @Mock
    private ProcedureValidator procedureValidator;

    /**
     * Mocked version of ProcedureRepository.
     */
    @Mock
    private ProcedureRepository procedureRepository;

    /**
     * Mocked version of WorkstationValidator.
     */
    @Mock
    private WorkstationValidator workstationValidator;

    /**
     * Mocked version of ProcessStepValidator.
     */
    @Mock
    private ProcessStepValidator processStepValidator;

    /**
     * Mocked version of StateValidator.
     */
    @Mock
    private StateValidator stateValidator;

    /**
     * Mocked version of ValueValidator.
     */
    @Mock
    private ValueValidator valueValidator;

    /**
     * Usual instance of Procedure to test and verify create, update and delete methods.
     */
    @Mock
    private Procedure procedure;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setUpProcedure();
    }

    /**
     * setup method for procedures
     */
    private void setUpProcedure() {
        procedure = new Procedure();

        procedure.setId("1337");
        procedure.setProcessStep(new ProcessStep());
        procedure.setStateHistory(new StateHistory());

        List<Value> values = new ArrayList<>();
        values.add(new Value());

        procedure.setValues(values);
    }

    /**
     * Test create validation proc step null.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationProcStepNull() throws ValidationException, CreationException {
        procedure.setProcessStep(null);
        when(procedureValidator.validate(procedure)).thenCallRealMethod();

        procedureService.create(procedure);
    }

    /**
     * Test create validation state history null.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationStateHistoryNull() throws ValidationException, CreationException {
        procedure.setStateHistory(null);
        when(procedureValidator.validate(procedure)).thenCallRealMethod();

        procedureService.create(procedure);
    }

    /**
     * Test create validation values null.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationValuesNull() throws ValidationException, CreationException {
        procedure.setValues(null);
        when(procedureValidator.validate(procedure)).thenCallRealMethod();

        procedureService.create(procedure);
    }

    /**
     * Test create validation procedure null.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationProcedureNull() throws ValidationException, CreationException {
        procedure = null;
        when(procedureValidator.validate(null)).thenCallRealMethod();

        procedureService.create(procedure);
    }

    /**
     * Test update validation proc step null.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationProcStepNull() throws ValidationException, UpdateException {
        procedure.setProcessStep(null);
        when(procedureValidator.validate(procedure)).thenCallRealMethod();

        procedureService.update(procedure);
    }

    /**
     * Test update validation state history null.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationStateHistoryNull() throws ValidationException, UpdateException {
        procedure.setStateHistory(null);
        when(procedureValidator.validate(procedure)).thenCallRealMethod();

        procedureService.update(procedure);
    }

    /**
     * Test update validation values null.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationValuesNull() throws ValidationException, UpdateException {
        procedure.setValues(null);
        when(procedureValidator.validate(procedure)).thenCallRealMethod();

        procedureService.update(procedure);
    }

    /**
     * Test update validation procedure null.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationProcedureNull() throws ValidationException, UpdateException {
        procedure = null;
        when(procedureValidator.validate(null)).thenCallRealMethod();

        procedureService.update(procedure);
    }

    /**
     * Test remove validation proc step null.
     *
     * @throws DeletionException the deletion exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = DeletionException.class)
    public void testRemoveValidationProcStepNull() throws DeletionException, ValidationException {
        procedure.setProcessStep(null);
        when(procedureValidator.validate(any())).thenCallRealMethod();

        procedureService.delete(procedure);
        verify(procedureValidator, times(1)).validate(procedure);
        verify(procedureRepository, times(1)).remove(procedure);
    }

    /**
     * Test remove validation state history null.
     *
     * @throws DeletionException the deletion exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = DeletionException.class)
    public void testRemoveValidationStateHistoryNull() throws DeletionException, ValidationException {
        procedure.setStateHistory(null);
        when(procedureValidator.validate(any())).thenCallRealMethod();

        procedureService.delete(procedure);
        verify(procedureValidator, times(1)).validate(procedure);
        verify(procedureRepository, times(1)).remove(procedure);
    }

    /**
     * Test remove validation values null.
     *
     * @throws DeletionException the deletion exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = DeletionException.class)
    public void testRemoveValidationValuesNull() throws DeletionException, ValidationException {
        procedure.setValues(null);
        when(procedureValidator.validate(any())).thenCallRealMethod();

        procedureService.delete(procedure);
        verify(procedureValidator, times(1)).validate(procedure);
        verify(procedureRepository, times(1)).remove(procedure);
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        List<Procedure> myList = new ArrayList<>();
        Procedure p1 = new Procedure();
        p1.setId("420");
        procedure.setProcessStep(new ProcessStep());
        myList.add(procedure);
        myList.add(p1);

        when(procedureRepository.findAll()).thenReturn(myList);
        assertEquals(procedureService.getAll(), myList);
        verify(procedureRepository, times(1)).findAll();
        assertEquals(procedureService.getAll().get(0), procedure);
        verify(procedureRepository, times(2)).findAll();
        assertEquals(procedureService.getAll().get(1), p1);
        verify(procedureRepository, times(3)).findAll();
    }

    /**
     * Test get by id null.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdNull() throws InvalidIdException {
        procedureService.getById(null);
    }

    /**
     * Test get by id empty.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdEmpty() throws InvalidIdException {
        procedureService.getById("");
    }

    /**
     * Test get by workstation successful.
     *
     * @throws FindByException the find by exception
     * @throws ValidationException the validation exception
     */
    @Test
    public void testGetByWorkstationSuccessful() throws FindByException, ValidationException {
        Workstation w = Mockito.mock(Workstation.class);
        Procedure proc = Mockito.mock(Procedure.class);
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(proc);

        when(workstationValidator.validate(w)).thenReturn(true);
        when(procedureRepository.findByWorkstation(any())).thenReturn(procedureList);

        assertEquals(procedureService.getByWorkstation(w).get(0), proc);
        verify(procedureRepository, times(1)).findByWorkstation(w);
        verify(workstationValidator, times(1)).validate(w);
    }

    /**
     * Test get by process step successful.
     *
     * @throws FindByException the find by exception
     * @throws ValidationNullPointerException the validation null pointer exception
     */
    @Test
    public void testGetByProcessStepSuccessful() throws FindByException, ValidationNullPointerException {
        ProcessStep ps = Mockito.mock(ProcessStep.class);
        Procedure proc = Mockito.mock(Procedure.class);
        proc.setProcessStep(ps);
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(proc);

        when(processStepValidator.validate(ps)).thenReturn(true);
        when(procedureRepository.findByProcessStep(ps)).thenReturn(procedureList);

        assertEquals(procedureService.getByProcessStep(ps).get(0), proc);
        verify(procedureRepository, times(1)).findByProcessStep(ps);
        verify(processStepValidator, times(1)).validate(ps);
    }

    /**
     * Test get by state successful.
     *
     * @throws FindByException the find by exception
     * @throws ValidationException the validation exception
     */
    @Test
    public void testGetByStateSuccessful() throws FindByException, ValidationException {
        State s = Mockito.mock(State.class);
        Procedure proc = Mockito.mock(Procedure.class);
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(proc);

        when(stateValidator.validate(s)).thenReturn(true);
        when(procedureRepository.findByState(s)).thenReturn(procedureList);

        assertEquals(procedureService.getByState(s).get(0), proc);
        verify(procedureRepository, times(1)).findByState(s);
        verify(stateValidator, times(1)).validate(s);
    }

    /**
     * Test get by state history successful.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByStateHistorySuccessful() throws FindByException {
        StateHistory sh = Mockito.mock(StateHistory.class);
        Procedure proc = Mockito.mock(Procedure.class);
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(proc);

        when(procedureRepository.findByStateHistory(sh)).thenReturn(procedureList);

        assertEquals(procedureService.getByStateHistory(sh).get(0), proc);
        verify(procedureRepository, times(1)).findByStateHistory(sh);
    }

    /**
     * Test get by value successful.
     *
     * @throws FindByException the find by exception
     * @throws ValidationException the validation exception
     */
    @Test
    public void testGetByValueSuccessful() throws FindByException, ValidationException {
        Value v = Mockito.mock(Value.class);
        Procedure proc = Mockito.mock(Procedure.class);
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(proc);

        when(valueValidator.validate(v)).thenReturn(true);
        when(procedureRepository.findByValue(v)).thenReturn(procedureList);

        assertEquals(procedureService.getByValue(v).get(0), proc);
        verify(procedureRepository, times(1)).findByValue(v);
        verify(valueValidator, times(1)).validate(v);
    }
}