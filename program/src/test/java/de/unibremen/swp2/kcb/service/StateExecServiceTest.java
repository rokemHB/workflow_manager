package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.persistence.statemachine.StateExecRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test Class to Test {@link StateExecService}
 */
public class StateExecServiceTest {

    /**
     * Injected instance of service
     */
    @InjectMocks
    private StateExecService stateExecService;

    /**
     * Mocked version of stateExec
     */
    @Mock
    private StateExec stateExec;

    /**
     * Mocked version of repository
     */
    @Mock
    private StateExecRepository stateExecRepository;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test create state exec.
     *
     * @throws CreationException the creation exception
     */
    @Test
    public void testCreateStateExec() throws CreationException {
        Assert.assertNull(stateExecService.create(stateExec));
    }

    /**
     * Test update state exec.
     *
     * @throws UpdateException the update exception
     */
    @Test
    public void testUpdateStateExec() throws UpdateException {
        Assert.assertNull(stateExecService.update(stateExec));
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        assertEquals(stateExecRepository.findAll(), stateExecService.getAll());
    }

    /**
     * Test delete.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDelete() throws DeletionException {
        stateExecService.delete(null);
    }

    /**
     * Test calculate transition time null.
     */
    @Test
    public void testCalculateTransitionTimeNull() {
        assertEquals(-1, stateExecService.calculateTransitionTime(null));
    }

    /**
     * Test calculate transition time null two.
     */
    @Test
    public void testCalculateTransitionTimeNullTwo() {
        when(stateExec.getStartedAt()).thenReturn(null);
        assertEquals(-1, stateExecService.calculateTransitionTime(stateExec));
    }

}
