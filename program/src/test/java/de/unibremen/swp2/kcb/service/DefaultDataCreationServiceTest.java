package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.ValidationPattern;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Test class to test {@link DefaultDataCreationService}
 *
 * @author Arvid
 */
public class DefaultDataCreationServiceTest {

    /**
     * Mocked version of entity Manager
     */
    EntityManager em = mock(EntityManager.class, RETURNS_DEEP_STUBS);
    /**
     * Injected instance of defaultDataCreationService
     */
    @InjectMocks
    private DefaultDataCreationService defaultDataCreationService;

    /**
     * Mocked version of validationPatternList
     */
    @Mock
    private List<ValidationPattern> validationPatternList;

    /**
     * Sets up tests.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test create default carrier type val pattern.
     */
    @Test
    public void testCreateDefaultCarrierTypeValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultCarrierTypeValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default id val pattern.
     */
    @Test
    public void testCreateDefaultIdValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultIdValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default carrier id val pattern.
     */
    @Test
    public void testCreateDefaultCarrierIdValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultCarrierIdValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default job name val pattern.
     */
    @Test
    public void testCreateDefaultJobNameValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultJobNameValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default state name val pattern.
     */
    @Test
    public void testCreateDefaultStateNameValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultStateNameValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default state machine name val pattern.
     */
    @Test
    public void testCreateDefaultStateMachineNameValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultStateMachineNameValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default workstation name val pattern.
     */
    @Test
    public void testCreateDefaultWorkstationNameValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultWorkstationNameValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default username val pattern.
     */
    @Test
    public void testCreateDefaultUsernameValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultUsernameValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default process step name val pattern.
     */
    @Test
    public void testCreateDefaultProcessStepNameValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultProcessStepNameValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default process chain name val pattern.
     */
    @Test
    public void testCreateDefaultProcessChainNameValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultProcessChainNameValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default name val pattern.
     */
    @Test
    public void testCreateDefaultNameValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultNameValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default email val pattern.
     */
    @Test
    public void testCreateDefaultEmailValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultEmailValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default password val pattern.
     */
    @Test
    public void testCreateDefaultPasswordValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultPasswordValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default assembly alloy val pattern.
     */
    @Test
    public void testCreateDefaultAssemblyAlloyValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultAssemblyAlloyValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default assembly comment val pattern.
     */
    @Test
    public void testCreateDefaultAssemblyCommentValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultAssemblyCommentValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default assembly id val pattern.
     */
    @Test
    public void testCreateDefaultAssemblyIdValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultAssemblyIdValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default position val pattern.
     */
    @Test
    public void testCreateDefaultPositionValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultPositionValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default parameter field val pattern.
     */
    @Test
    public void testCreateDefaultParameterFieldValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultParameterFieldValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default priority name field val pattern.
     */
    @Test
    public void testCreateDefaultPriorityNameFieldValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultPriorityNameFieldValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default value val pattern.
     */
    @Test
    public void testCreateDefaultValueValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultValueValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default integer val pattern.
     */
    @Test
    public void testCreateDefaultIntegerValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultIntegerValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default unit val pattern.
     */
    @Test
    public void testCreateDefaultUnitValPattern() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultUnitValPattern();
        verify(em, atLeast(1)).getTransaction();
    }

    /**
     * Test create default old active job value.
     */
    @Test
    public void testCreateDefaultOldActiveJobValue() {
        when(em.createNamedQuery(anyString()).setParameter(anyString(), anyString()).getResultList()).thenReturn(validationPatternList);
        defaultDataCreationService.createDefaultOldActiveJobValue();
        verify(em, atLeast(1)).getTransaction();
    }
}
