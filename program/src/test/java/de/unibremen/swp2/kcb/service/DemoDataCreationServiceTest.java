package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * The type Demo data creation service test.
 */
public class DemoDataCreationServiceTest {

    /**
     * Mocked version of entity Manager
     */
    EntityManager em = mock(EntityManager.class, RETURNS_DEEP_STUBS);
    /**
     * Injected instance of DemoDataCreationService
     */
    @InjectMocks
    private DemoDataCreationService demoDataCreationService;
    /**
     * Mocked version of userList
     */
    @Mock
    private List<User> userList;

    /**
     * Mocked version of workstationList
     */
    @Mock
    private List<Workstation> workstationList;

    /**
     * Mocked version of parameterList
     */
    @Mock
    private List<Parameter> parameterList;

    /**
     * Mocked version of stateMachineList
     */
    @Mock
    private List<StateMachine> stateMachineList;

    /**
     * Mocked version of carrierTypeList
     */
    @Mock
    private List<CarrierType> carrierTypeList;

    /**
     * Mocked version of processStepList
     */
    @Mock
    private List<ProcessStep> processStepList;

    /**
     * Mocked version of stockList
     */
    @Mock
    private List<Stock> stockList;

    /**
     * Mocked version of state
     */
    @Mock
    private State state;

    /**
     * Mocked version of assemblyList
     */
    @Mock
    private List<Assembly> assemblyList;

    /**
     * Mocked version of processChainList
     */
    @Mock
    private List<ProcessChain> processChainList;

    /**
     * Sets up tests.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test create demo users.
     */
    @Test
    public void testCreateDemoUsers() {
        when(em.createNamedQuery(any()).getResultList()).thenReturn(userList);
        demoDataCreationService.createDemoUsers();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create demo transport.
     */
    @Test
    public void testCreateDemoTransport() {
        //Bitte einmal weggucken Karsten
        when(em.createNamedQuery(any()).getResultList()).thenReturn(userList);
        demoDataCreationService.createDemoTransport();
        verify(em, times(1)).createNamedQuery(any());
    }

    /**
     * Test create demo workstation.
     */
    @Test
    public void testCreateDemoWorkstation() {
        when(em.createNamedQuery(any()).getResultList()).thenReturn(userList);
        demoDataCreationService.createDemoWorkstation();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create demo parameter.
     */
    @Test
    public void testCreateDemoParameter() {
        demoDataCreationService.createDemoParameter();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create demo process step.
     */
    @Test
    public void testCreateDemoProcessStep() {
        when(em.createNamedQuery(any()).getResultList()).thenReturn(workstationList);
        when(em.createNamedQuery(any()).getResultList()).thenReturn(parameterList);
        when(em.createNamedQuery(any()).getResultList()).thenReturn(stateMachineList);
        when(em.createNamedQuery(any()).getResultList()).thenReturn(carrierTypeList);

        demoDataCreationService.createDemoProcessStep();

        verify(em, times(4)).createNamedQuery(any());
    }

    /**
     * Test create demo process chain.
     */
    @Test
    public void testCreateDemoProcessChain() {
        when(em.createNamedQuery(any()).getResultList()).thenReturn(processStepList);
        demoDataCreationService.createDemoProcessChain();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create demo priorities.
     */
    @Test
    public void testCreateDemoPriorities() {
        demoDataCreationService.createDemoPriorities();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create demo carriers and assemblies.
     */
    @Test
    public void testCreateDemoCarriersAndAssemblies() {
        when(em.createNamedQuery(any()).getResultList()).thenReturn(carrierTypeList);
        when(em.createNamedQuery(any()).getResultList()).thenReturn(stockList);

        demoDataCreationService.createDemoCarriersAndAssemblies();

        verify(em, times(2)).createNamedQuery(any());
    }

    /**
     * Test create demo jobs.
     */
    @Test
    public void testCreateDemoJobs() {
        when(em.createNamedQuery(any()).getResultList()).thenReturn(assemblyList);
        when(em.createNamedQuery(any()).getResultList()).thenReturn(processChainList);

        demoDataCreationService.createDemoJobs();

        verify(em, times(2)).createNamedQuery(any());
    }
}
