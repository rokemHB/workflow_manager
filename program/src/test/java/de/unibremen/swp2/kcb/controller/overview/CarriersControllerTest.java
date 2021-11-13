package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.service.CarrierService;
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
 * Test Class to Test {@link CarriersController}
 *
 * @author Robin
 */
public class CarriersControllerTest {

    /**
     * Injected instance of CarriersController.
     */
    @InjectMocks
    CarriersController carriersController;

    /**
     * Mocked version of carrierService entity.
     */
    @Mock
    CarrierService carrierService;

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
        carriersController.init();
        List<Carrier> empty = new ArrayList<>();
        when(carrierService.getAll()).thenReturn(empty);
        verify(carrierService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of CarriersController. Tests whether getAll() gets called in
     * CarrierService.
     */
    @Test
    public void testRefresh() {
        List<Carrier> steps = new ArrayList<>();
        steps.add(new Carrier());
        when(carrierService.getAll()).thenReturn(steps);
        carriersController.refresh();
        verify(carrierService, times(1)).getAll();
        assertEquals(carriersController.getEntities(), steps);
    }

    /**
     * Test refresh fail.
     */
    @Test
    public void testRefreshFail() {
        when(carrierService.getAll()).thenReturn(null);
        carriersController.refresh();
        verify(carrierService, times(1)).getAll();
        List<Carrier> empty = new ArrayList<>();
        assertEquals(carriersController.getEntities(), empty);
    }

    /**
     * Tests the getById() method of CarriersController.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetById() throws InvalidIdException {
        Carrier dummy = new Carrier();
        dummy.setId("111");
        when(carrierService.getById("111")).thenReturn(dummy);
        assertEquals(carriersController.getById("111"), dummy);
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(carrierService.getById("123")).thenThrow(new InvalidIdException("fail"));
        assertNull(carriersController.getById("123"));
    }

    /**
     * Tests the getByCarrierType() Method.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByCarrierType() throws FindByException {
        Carrier dummy = new Carrier();
        CarrierType ct1 = new CarrierType();
        dummy.setCarrierType(ct1);
        List<Carrier> res = new ArrayList<>();
        res.add(dummy);
        when(carrierService.getByCarrierType(ct1)).thenReturn(res);
        assertEquals(carriersController.getByCarrierType(ct1), res);
    }

    /**
     * Tests the getByCarrierType() Method in case carrierType was not found.
     *
     * @throws FindByException when failed.
     */
    @Test
    public void testGetByCarrierTypeFail() throws FindByException {
        CarrierType ct1 = new CarrierType();
        when(carrierService.getByCarrierType(ct1)).thenThrow(new FindByException("fail"));
        List<Carrier> empty = new ArrayList<>();
        assertEquals(carriersController.getByCarrierType(ct1), empty);
    }

    /**
     * Test get by location.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByLocation() throws FindByException {
        Location location = new Stock();
        Carrier dummy = new Carrier();
        List<Carrier> res = new ArrayList<>();
        when(carrierService.getByLocation(location)).thenReturn(res);
        assertEquals(carriersController.getByLocation(location), res);
        verify(carrierService, times(1)).getByLocation(location);
    }

    /**
     * Test get by location fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByLocationFail() throws FindByException {
        Location location = new Stock();
        when(carrierService.getByLocation(location)).thenThrow(new FindByException("fail"));
        List<Carrier> empty = new ArrayList<>();
        assertEquals(carriersController.getByLocation(location), empty);
    }

    /**
     * Test get by carrier type and location.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByCarrierTypeAndLocation() throws FindByException {
        Location location = new Stock();
        CarrierType ct1 = new CarrierType();
        Carrier dummy = new Carrier();
        List<Carrier> res = new ArrayList<>();
        when(carrierService.getByCarrierTypeAndLocation(ct1, location)).thenReturn(res);
        assertEquals(carriersController.getByCarrierTypeAndLocation(ct1, location), res);
    }

    /**
     * Test get by carrier type and location fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByCarrierTypeAndLocationFail() throws FindByException {
        Location location = new Stock();
        CarrierType ct1 = new CarrierType();
        when(carrierService.getByCarrierTypeAndLocation(ct1, location)).thenThrow(new FindByException("fail"));
        List<Carrier> empty = new ArrayList<>();
        assertEquals(carriersController.getByCarrierTypeAndLocation(ct1, location), empty);
    }
}