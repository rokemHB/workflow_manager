package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.service.ProcessChainService;
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
 * Test Class to Test {@link ProcessChainsController}
 *
 * @author Robin
 * @author Arvid
 */
public class ProcessChainsControllerTest {

    /**
     * Injected instance of assembliesController.
     */
    @InjectMocks
    ProcessChainsController processChainsController;

    /**
     * Mocked version of assemblyService entity.
     */
    @Mock
    ProcessChainService processChainService;

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
        processChainsController.init();
        List<ProcessChain> empty = new ArrayList<>();
        when(processChainService.getAll()).thenReturn(empty);
        verify(processChainService, times(1)).getAll();
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        List<ProcessChain> list = new ArrayList<>();
        ProcessChain processChain = new ProcessChain();
        list.add(processChain);
        when(processChainService.getAll()).thenReturn(list);
        assertEquals(processChainsController.getAll(), list);
        verify(processChainService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of processStepController. Tests whether getAll() gets called
     * in processStepService.
     */
    @Test
    public void testRefresh() {
        List<ProcessChain> processChains = new ArrayList<>();
        processChains.add(new ProcessChain());
        when(processChainService.getAll()).thenReturn(processChains);
        processChainsController.refresh();
        verify(processChainService, times(1)).getAll();
        assertEquals(processChainsController.getEntities(), processChains);
    }

    /**
     * Test refresh fail.
     */
    @Test
    public void testRefreshFail() {
        when(processChainService.getAll()).thenReturn(null);
        processChainsController.refresh();
        verify(processChainService, times(1)).getAll();
        List<ProcessChain> empty = new ArrayList<>();
        assertEquals(processChainsController.getEntities(), empty);
    }

    /**
     * Tests the getById() method of assembliesController.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetById() throws InvalidIdException {
        ProcessChain dummy = new ProcessChain();
        dummy.setId("111");
        when(processChainService.getById("111")).thenReturn(dummy);
        assertEquals(processChainsController.getById("111"), dummy);
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(processChainService.getById("123")).thenThrow(new InvalidIdException("fail"));
        assertNull(processChainsController.getById("123"));
    }

    /**
     * Test by chain.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByChain() throws FindByException {
        ProcessChain processChain = new ProcessChain();
        List<ProcessChain> list = new ArrayList<>();
        ProcessStep processStep = new ProcessStep();
        List<ProcessStep> steps = new ArrayList<>();
        list.add(processChain);
        steps.add(processStep);

        when(processChainService.getByChain(steps)).thenReturn(list);
        assertEquals(processChainsController.getByChain(steps), list);
        verify(processChainService, times(1)).getByChain(steps);
    }

    /**
     * Test get by chain fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByChainFail() throws FindByException {
        ProcessStep processStep = new ProcessStep();
        List<ProcessStep> steps = new ArrayList<>();
        List<ProcessChain> empty = new ArrayList<>();

        when(processChainService.getByChain(steps)).thenThrow(FindByException.class);
        assertEquals(processChainsController.getByChain(steps), empty);
    }

}