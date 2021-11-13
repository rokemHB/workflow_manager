package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.service.WorkstationService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.shiro.authz.UnauthorizedException;
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
 * Test Class to Test {@link WorkstationsController}
 *
 * @author Robin
 */
public class WorkstationsControllerTest {

    /**
     * Injected instance of workstationsController.
     */
    @InjectMocks
    WorkstationsController workstationsController;

    /**
     * Mocked version of assemblyService entity.
     */
    @Mock
    WorkstationService workstationService;

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
        workstationsController.init();
        List<Workstation> empty = new ArrayList<>();
        when(workstationService.getAll()).thenReturn(empty);
        verify(workstationService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of workstationsController. Tests whether getAll() gets called
     * in workstationService.
     */
    @Test
    public void testRefresh() {
        List<Workstation> ws = new ArrayList<>();
        ws.add(new Workstation());
        when(workstationService.getAll()).thenReturn(ws);
        workstationsController.refresh();
        verify(workstationService, times(1)).getAll();
        assertEquals(workstationsController.getEntities(), ws);
    }

    /**
     * Test get active.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetActive() throws FindByException {
        List<Workstation> list = new ArrayList<>();
        Workstation workstation = new Workstation();
        list.add(workstation);
        when(workstationService.getActive()).thenReturn(list);
        assertEquals(workstationsController.getActive(), list);
        verify(workstationService, times(1)).getActive();
    }

    /**
     * Test get active fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetActiveFail() throws FindByException {
        List<Workstation> empty = new ArrayList<>();
        when(workstationService.getActive()).thenThrow(FindByException.class);
        assertEquals(workstationsController.getActive(), empty);
        verify(workstationService, times(1)).getActive();
    }

    /**
     * Test get inactive.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetInactive() throws FindByException {
        List<Workstation> list = new ArrayList<>();
        Workstation workstation = new Workstation();
        list.add(workstation);
        when(workstationService.getInactive()).thenReturn(list);
        assertEquals(workstationsController.getInactive(), list);
        verify(workstationService, times(1)).getInactive();
    }

    /**
     * Test get inactive fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetInactiveFail() throws FindByException {
        List<Workstation> empty = new ArrayList<>();
        when(workstationService.getInactive()).thenThrow(FindByException.class);
        assertEquals(workstationsController.getInactive(), empty);
        verify(workstationService, times(1)).getInactive();
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws UnauthorizedException the unauthorized exception
     */
    @Test
    public void testGetById() throws InvalidIdException, UnauthorizedException {
        Workstation dummy = new Workstation();
        dummy.setId("111");
        when(workstationService.getById("111")).thenReturn(dummy);
        assertEquals(workstationsController.getById("111"), dummy);
        verify(workstationService, times(1)).getById(anyString());
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws UnauthorizedException the unauthorized exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException, UnauthorizedException {
        when(workstationService.getById("123")).thenThrow(new InvalidIdException("fail"));
        assertNull(workstationsController.getById("123"));
        verify(workstationService, times(1)).getById(anyString());
    }

    /**
     * Test get by position.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByPosition() throws FindByException {
        Workstation dummy = new Workstation();
        List<Workstation> res = new ArrayList<>();
        res.add(dummy);
        when(workstationService.getByPosition("position")).thenReturn(res);
        assertEquals(workstationsController.getByPosition("position"), res);
        verify(workstationService, times(1)).getByPosition("position");
    }

    /**
     * Test get by position fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByPositionFail() throws FindByException {
        Location location = new Stock();
        when(workstationService.getByPosition("position")).thenThrow(FindByException.class);
        List<Workstation> empty = new ArrayList<>();
        assertEquals(workstationsController.getByPosition("position"), empty);
    }

}
