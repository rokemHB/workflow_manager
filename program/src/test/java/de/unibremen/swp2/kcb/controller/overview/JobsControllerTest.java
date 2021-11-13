package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.service.JobService;
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
 * Test Class to Test {@link JobsController}
 *
 * @author Marc
 * @author Robin
 */
public class JobsControllerTest {

    /**
     * Injected instance of ProcessStepsController.
     */
    @InjectMocks
    JobsController jobsController;

    /**
     * Mocked version of processStepService entity.
     */
    @Mock
    JobService jobService;

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
        jobsController.init();
        List<Job> empty = new ArrayList<>();
        when(jobService.getActive()).thenReturn(empty);
        verify(jobService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of jobsController. Tests whether getAll() gets called in
     * jobsService.
     */
    @Test
    public void testRefresh() {
        List<Job> steps = new ArrayList<>();
        steps.add(new Job());
        when(jobService.getAll()).thenReturn(steps);
        jobsController.refresh();
        verify(jobService, times(1)).getAll();
        assertEquals(jobsController.getEntities(), steps);
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        Job dummy = new Job();
        dummy.setId("111");
        when(jobService.getById("111")).thenReturn(dummy);
        assertEquals(jobsController.getById("111"), dummy);
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(jobService.getById("123")).thenThrow(InvalidIdException.class);
        assertNull(jobsController.getById("123"));
    }

    /**
     * Test get by active.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByActive() throws FindByException {
        List<Job> empty = new ArrayList<>();
        when(jobService.getActive()).thenReturn(empty);
        assertEquals(jobsController.getActive(), empty);
        verify(jobService, times(1)).getActive();
    }

    /**
     * Test get by process chain.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByProcessChain() throws FindByException {
        List<Job> list = new ArrayList<>();
        Job job = new Job();
        list.add(job);
        ProcessChain processChain = new ProcessChain();
        when(jobService.getByProcessChain(processChain)).thenReturn(list);
        assertEquals(jobsController.getByProcessChain(processChain), list);
        verify(jobService, times(1)).getByProcessChain(processChain);
    }

    /**
     * Test get by process chain fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByProcessChainFail() throws FindByException {
        List<Job> list = new ArrayList<>();
        ProcessChain processChain = new ProcessChain();
        when(jobService.getByProcessChain(processChain)).thenThrow(FindByException.class);
        assertEquals(jobsController.getByProcessChain(processChain), list);
        verify(jobService, times(1)).getByProcessChain(processChain);
    }

    /**
     * Test get pending.
     */
    @Test
    public void testGetPending() {
        List<Job> list = new ArrayList<>();
        Job job = new Job();
        list.add(job);
        when(jobService.getPending()).thenReturn(list);
        assertEquals(jobsController.getPending(), list);
        verify(jobService, times(1)).getPending();
    }
}