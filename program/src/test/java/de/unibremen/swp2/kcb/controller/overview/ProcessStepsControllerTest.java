package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.service.ProcessStepService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link ProcessStepsController}.
 *
 * @author Marc
 * @author Robin
 */
public class ProcessStepsControllerTest {

    /**
     * Injected instance of ProcessStepsController.
     */
    @InjectMocks
    ProcessStepsController processStepsController;

    /**
     * Mocked version of processStepService entity.
     */
    @Mock
    ProcessStepService processStepService;

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
        processStepsController.init();
        List<ProcessStep> empty = new ArrayList<>();
        when(processStepService.getAll()).thenReturn(empty);
        verify(processStepService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of processStepController. Tests whether getAll() gets called
     * in processStepService.
     */
    @Test
    public void testRefresh() {
        List<ProcessStep> steps = new ArrayList<>();
        steps.add(new ProcessStep());
        when(processStepService.getAll()).thenReturn(steps);
        processStepsController.refresh();
        verify(processStepService, times(1)).getAll();
        assertEquals(processStepsController.getEntities(), steps);
    }

    /**
     * Tests the getById() method of processStepController.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetById() throws InvalidIdException {
        ProcessStep dummy = new ProcessStep();
        dummy.setId("111");
        when(processStepService.getById("111")).thenReturn(dummy);
        assertEquals(processStepsController.getById("111"), dummy);
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(processStepService.getById("123")).thenThrow(InvalidIdException.class);
        assertNull(processStepsController.getById("123"));
    }

    /**
     * Tests the getByName() Method with a List containing one and a List containing two
     * processSteps respectively.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByName() throws FindByException {
        ProcessStep dummy0 = new ProcessStep();
        ProcessStep dummy1 = new ProcessStep();
        ProcessStep dummy2 = new ProcessStep();
        dummy0.setName("Dummkopf");
        dummy1.setName("Dummkopf");
        dummy1.setName("Schlaumeier");
        List<ProcessStep> nameList2 = new ArrayList<>();
        List<ProcessStep> nameList1 = new ArrayList<>();
        nameList2.add(dummy0);
        nameList2.add(dummy1);
        nameList1.add(dummy2);
        when(processStepService.getByName("Dummkopf")).thenReturn(nameList2);
        when(processStepService.getByName("Schlaumeier")).thenReturn(nameList1);
        assertEquals(processStepsController.getByName("Dummkopf"), nameList2);
        assertEquals(processStepsController.getByName("Schlaumeier"), nameList1);
    }

    /**
     * Tests the getByName() Method in case name was not found.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByNameFail() throws FindByException {
        List<ProcessStep> empty = new ArrayList<>();
        when(processStepService.getByName("test")).thenThrow(FindByException.class);
        assertEquals(processStepsController.getByName("test"), empty);
    }

    /**
     * Tests the getByEstDuration() Method with a List containing one and a List containing two
     * processSteps respectively.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByEstDuration() throws FindByException {
        ProcessStep dummy0 = new ProcessStep();
        ProcessStep dummy1 = new ProcessStep();
        ProcessStep dummy2 = new ProcessStep();
        dummy0.setEstDuration(1);
        dummy1.setEstDuration(1);
        dummy2.setEstDuration(420);
        List<ProcessStep> nameList2 = new ArrayList<>();
        List<ProcessStep> nameList1 = new ArrayList<>();
        nameList2.add(dummy0);
        nameList2.add(dummy1);
        nameList1.add(dummy2);
        when(processStepService.getByEstDuration(1)).thenReturn(nameList2);
        when(processStepService.getByEstDuration(420)).thenReturn(nameList1);
        assertEquals(processStepsController.getByEstDuration(1), nameList2);
        assertEquals(processStepsController.getByEstDuration(420), nameList1);
    }

    /**
     * Tests the getByEstDuration() Method in case estDuration was not found.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByEstDurationFail() throws FindByException {
        List<ProcessStep> empty = new ArrayList<>();
        when(processStepService.getByEstDuration(anyInt())).thenThrow(FindByException.class);
        assertEquals(processStepsController.getByEstDuration(anyInt()), empty);
    }

    /**
     * Tests the getByStateMachine() Method with a List containing one and a List containing two
     * processSteps respectively.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByStateMachine() throws FindByException {
        ProcessStep dummy0 = new ProcessStep();
        ProcessStep dummy1 = new ProcessStep();
        ProcessStep dummy2 = new ProcessStep();
        StateMachine sm1 = new StateMachine();
        StateMachine sm2 = new StateMachine();
        dummy0.setStateMachine(sm1);
        dummy1.setStateMachine(sm1);
        dummy2.setStateMachine(sm2);
        List<ProcessStep> nameList2 = new ArrayList<>();
        List<ProcessStep> nameList1 = new ArrayList<>();
        nameList2.add(dummy0);
        nameList2.add(dummy1);
        nameList1.add(dummy2);
        when(processStepService.getByStateMachine(sm1)).thenReturn(nameList2);
        when(processStepService.getByStateMachine(sm2)).thenReturn(nameList1);
        assertEquals(processStepsController.getByStateMachine(sm1), nameList2);
        assertEquals(processStepsController.getByStateMachine(sm2), nameList1);
    }

    /**
     * Tests the getByStateMachine() Method in case nothing was not found.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByStateMachineFail() throws FindByException {
        List<ProcessStep> empty = new ArrayList<>();
        StateMachine stateMachine = new StateMachine();
        stateMachine.setName("CaptainNullPointer");
        when(processStepService.getByStateMachine(stateMachine)).thenThrow(FindByException.class);
        assertEquals(processStepsController.getByStateMachine(stateMachine), empty);
    }

    /**
     * Tests the getByWorkstation() Method in case nothing was not found.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByWorkstationFail() throws FindByException {
        List<ProcessStep> empty = new ArrayList<>();
        Workstation workstation = new Workstation();
        workstation.setName("name");
        when(processStepService.getByWorkstation(workstation)).thenThrow(FindByException.class);
        assertEquals(processStepsController.getByWorkstation(workstation), empty);
    }

    /**
     * Tests the getByParameter() Method with a List containing one, two  and three Parameters for
     * processSteps respectively.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByParameter() throws FindByException {
        ProcessStep dummy0 = new ProcessStep();
        ProcessStep dummy1 = new ProcessStep();
        ProcessStep dummy2 = new ProcessStep();
        Parameter pm1 = new Parameter();
        Parameter pm2 = new Parameter();
        Parameter pm3 = new Parameter();
        List<Parameter> res1 = new ArrayList<>();
        List<Parameter> res2 = new ArrayList<>();
        List<Parameter> res3 = new ArrayList<>();
        res1.add(pm1);
        res1.add(pm2);
        res1.add(pm3);
        res2.add(pm2);
        res2.add(pm3);
        res3.add(pm3);
        dummy0.setParameters(res1);
        dummy1.setParameters(res2);
        dummy2.setParameters(res3);
        List<ProcessStep> threeSteps = new ArrayList<>(); // search for pm3
        List<ProcessStep> twoSteps = new ArrayList<>(); // search for pm2
        List<ProcessStep> oneStep = new ArrayList<>(); // search for pm1
        threeSteps.add(dummy0);
        threeSteps.add(dummy1);
        threeSteps.add(dummy2);
        twoSteps.add(dummy0);
        twoSteps.add(dummy1);
        oneStep.add(dummy2);
        when(processStepService.getByParameter(pm3)).thenReturn(threeSteps);
        when(processStepService.getByParameter(pm2)).thenReturn(twoSteps);
        when(processStepService.getByParameter(pm1)).thenReturn(oneStep);
        assertEquals(processStepsController.getByParameter(pm3), threeSteps);
        assertEquals(processStepsController.getByParameter(pm2), twoSteps);
        assertEquals(processStepsController.getByParameter(pm1), oneStep);
    }

    /**
     * Tests the getByParameterFail() Method in case estDuration was not found.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByParameterFail() throws FindByException {
        List<ProcessStep> empty = new ArrayList<>();
        Parameter parameter = new Parameter();
        parameter.setField("FieldName");
        when(processStepService.getByParameter(parameter)).thenThrow(FindByException.class);
        assertEquals(processStepsController.getByParameter(parameter), empty);
    }

    /**
     * Tests the getByOutputCarrierType() Method with a List containing one and a List containing
     * two processSteps respectively.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByOutputCarrierType() throws FindByException {
        ProcessStep dummy0 = new ProcessStep();
        ProcessStep dummy1 = new ProcessStep();
        ProcessStep dummy2 = new ProcessStep();
        CarrierType ct1 = new CarrierType();
        CarrierType ct2 = new CarrierType();
        dummy0.setOutput(ct1);
        dummy1.setOutput(ct1);
        dummy2.setOutput(ct2);
        List<ProcessStep> twoTypes = new ArrayList<>();
        List<ProcessStep> oneType = new ArrayList<>();
        twoTypes.add(dummy0);
        twoTypes.add(dummy1);
        oneType.add(dummy2);
        when(processStepService.getByOutputCarrierType(ct1)).thenReturn(twoTypes);
        when(processStepService.getByOutputCarrierType(ct2)).thenReturn(oneType);
        assertEquals(processStepsController.getByOutputCarrierType(ct1), twoTypes);
        assertEquals(processStepsController.getByOutputCarrierType(ct2), oneType);
    }

    /**
     * Tests the getByOutputCarrierType() Method in case estDuration was not found.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByOutputCarrierTypeFail() throws FindByException {
        List<ProcessStep> empty = new ArrayList<>();
        CarrierType ct = new CarrierType();
        ct.setName("ctOutName");
        when(processStepService.getByOutputCarrierType(ct)).thenThrow(FindByException.class);
        assertEquals(processStepsController.getByOutputCarrierType(ct), empty);
    }

    /**
     * Tests the getByPreparationCarrierType() Method with a List containing one and a List
     * containing two processSteps respectively.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByPreparationCarrierType() throws FindByException {
        ProcessStep dummy0 = new ProcessStep();
        ProcessStep dummy1 = new ProcessStep();
        ProcessStep dummy2 = new ProcessStep();
        CarrierType ct1 = new CarrierType();
        CarrierType ct2 = new CarrierType();
        dummy0.setPreparation(ct1);
        dummy1.setPreparation(ct1);
        dummy2.setPreparation(ct2);
        List<ProcessStep> twoTypes = new ArrayList<>();
        List<ProcessStep> oneType = new ArrayList<>();
        twoTypes.add(dummy0);
        twoTypes.add(dummy1);
        oneType.add(dummy2);
        when(processStepService.getByPreparationCarrierType(ct1)).thenReturn(twoTypes);
        when(processStepService.getByPreparationCarrierType(ct2)).thenReturn(oneType);
        assertEquals(processStepsController.getByPreparationCarrierType(ct1), twoTypes);
        assertEquals(processStepsController.getByPreparationCarrierType(ct2), oneType);
    }

    /**
     * Tests the getByPreparationCarrierType() Method in case estDuration was not found.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByPreparationCarrierTypeFail() throws FindByException {
        List<ProcessStep> empty = new ArrayList<>();
        CarrierType ct = new CarrierType();
        ct.setName("ctPreparationName");
        when(processStepService.getByPreparationCarrierType(ct)).thenThrow(FindByException.class);
        assertEquals(processStepsController.getByPreparationCarrierType(ct), empty);
    }

    /**
     * Tests the getByCarrierType() Method irrespective of output and preparation CarrierType.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByCarrierType() throws FindByException {
        ProcessStep dummy0 = new ProcessStep();
        ProcessStep dummy1 = new ProcessStep();
        ProcessStep dummy2 = new ProcessStep();
        CarrierType ct1 = new CarrierType();
        CarrierType ct2 = new CarrierType();
        CarrierType ct3 = new CarrierType();
        dummy0.setPreparation(ct1);
        dummy0.setOutput(ct3);
        dummy1.setPreparation(ct2);
        dummy1.setOutput(ct3);
        dummy2.setPreparation(ct3);
        dummy2.setOutput(ct1);
        List<ProcessStep> out1 = new ArrayList<>();
        List<ProcessStep> out2 = new ArrayList<>();
        out1.add(dummy2);
        out2.add(dummy0);
        out2.add(dummy1);
        List<ProcessStep> prep1 = new ArrayList<>();
        List<ProcessStep> prep2 = new ArrayList<>();
        prep1.add(dummy0);
        prep1.add(dummy2);
        when(processStepService.getByOutputCarrierType(ct1)).thenReturn(out1);
        when(processStepService.getByOutputCarrierType(ct3)).thenReturn(out2);
        when(processStepService.getByPreparationCarrierType(ct1)).thenReturn(prep1);
        when(processStepService.getByPreparationCarrierType(ct3)).thenReturn(prep2);
        List<ProcessStep> result1 = new ArrayList<>();
        List<ProcessStep> result2 = new ArrayList<>();
        result1.addAll(out1); // search ct1
        result1.addAll(prep1); // search ct1
        result2.addAll(out2); // search ct3
        result2.addAll(prep2); // search ct3
        assertEquals(processStepsController.getByCarrierType(ct1), result1);
        assertEquals(processStepsController.getByCarrierType(ct3), result2);
    }
}