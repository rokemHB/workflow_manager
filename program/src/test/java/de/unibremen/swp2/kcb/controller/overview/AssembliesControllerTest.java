package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.service.AssemblyService;
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
 * Test class to test {@link AssembliesController}
 *
 * @author Robin
 * @author Arvid
 */
public class AssembliesControllerTest {

    /**
     * Injected instance of assembliesController.
     */
    @InjectMocks
    AssembliesController assembliesController;

    /**
     * Mocked version of assemblyService entity.
     */
    @Mock
    AssemblyService assemblyService;

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
        assembliesController.init();
        List<Assembly> empty = new ArrayList<>();
        when(assemblyService.getAll()).thenReturn(empty);
        verify(assemblyService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of AssembliesController. Tests whether getAll() gets called in
     * processStepService.
     */
    @Test
    public void testRefresh() {
        List<Assembly> ass = new ArrayList<>();
        ass.add(new Assembly());
        when(assemblyService.getAll()).thenReturn(ass);
        assembliesController.refresh();
        verify(assemblyService, times(1)).getAll();
        assertEquals(assembliesController.getEntities(), ass);
    }

    /**
     * Test refresh fail.
     */
    @Test
    public void testRefreshFail() {
        when(assemblyService.getAll()).thenReturn(null);
        assembliesController.refresh();
        verify(assemblyService, times(1)).getAll();
        List<Assembly> empty = new ArrayList<>();
        assertEquals(assembliesController.getEntities(), empty);
    }

    /**
     * Tests the getById() method of assembliesController.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetById() throws InvalidIdException {
        Assembly dummy = new Assembly();
        dummy.setId("111");
        when(assemblyService.getById("111")).thenReturn(dummy);
        assertEquals(assembliesController.getById("111"), dummy);
        verify(assemblyService, times(1)).getById(anyString());
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(assemblyService.getById("123")).thenThrow(new InvalidIdException("fail"));
        assertNull(assembliesController.getById("123"));
        verify(assemblyService, times(1)).getById(anyString());
    }

    /**
     * Test get ready for transport.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetReadyForTransport() throws FindByException {
        List<Assembly> list = new ArrayList<>();
        Assembly assembly = new Assembly();
        list.add(assembly);
        when(assemblyService.getCollectable()).thenReturn(list);
        assertEquals(assembliesController.getReadyForTransport(), list);
        verify(assemblyService, times(1)).getCollectable();
    }

    /**
     * Test get ready for transport fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetReadyForTransportFail() throws FindByException {
        List<Assembly> empty = new ArrayList<>();
        assertEquals(assembliesController.getReadyForTransport(), empty);
        verify(assemblyService, times(1)).getCollectable();
    }

    /**
     * Test get by sample count.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetBySampleCount() throws FindByException {
        List<Assembly> empty = new ArrayList<>();
        Assembly assembly = new Assembly();
        empty.add(assembly);
        when(assemblyService.getBySampleCount(4)).thenReturn(empty);
        assertEquals(assembliesController.getBySampleCount(4), empty);
        verify(assemblyService, times(1)).getBySampleCount(4);
    }

    /**
     * Test get by sample count fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetBySampleCountFail() throws FindByException {
        List<Assembly> empty0 = new ArrayList<>();
        when(assemblyService.getBySampleCount(4)).thenThrow(new FindByException("fail"));
        assertEquals(assembliesController.getBySampleCount(4), empty0);
        verify(assemblyService, times(1)).getBySampleCount(4);
    }

    /**
     * Test get by alloy.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByAlloy() throws FindByException {
        List<Assembly> empty = new ArrayList<>();
        Assembly assembly = new Assembly();
        empty.add(assembly);
        when(assemblyService.getByAlloy("alloy")).thenReturn(empty);
        assertEquals(assembliesController.getByAlloy("alloy"), empty);
        verify(assemblyService, times(1)).getByAlloy("alloy");
    }

    /**
     * Test get by alloy fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByAlloyFail() throws FindByException {
        List<Assembly> empty0 = new ArrayList<>();
        when(assemblyService.getByAlloy("alloy")).thenThrow(new FindByException("fail"));
        assertEquals(assembliesController.getByAlloy("alloy"), empty0);
        verify(assemblyService, times(1)).getByAlloy("alloy");
    }

}