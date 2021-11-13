package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.persistence.locations.StockRepository;
import de.unibremen.swp2.kcb.persistence.locations.TransportRepository;
import de.unibremen.swp2.kcb.persistence.locations.WorkstationRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Test class to test {@link LocationService}
 *
 * @author Robin
 */
public class LocationServiceTest {

    /**
     * Injected instance of locationService
     */
    @InjectMocks
    private LocationService locationService;

    /**
     * Mocked version of workstationRepository
     */
    @Mock
    private WorkstationRepository workstationRepository;

    /**
     * Mocked version of transportRepository
     */
    @Mock
    private TransportRepository transportRepository;

    /**
     * Mocked version of stockRepository
     */
    @Mock
    private StockRepository stockRepository;

    /**
     * Mocked version of assemblyService
     */
    @Mock
    private AssemblyService assemblyService;

    /**
     * Mocked version of transport
     */
    @Mock
    private Transport transport;

    /**
     * Sets up tests.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test get all.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetAll() throws FindByException {
        List<Workstation> result1 = new ArrayList<>();
        List<Transport> result2 = new ArrayList<>();
        List<Stock> result3 = new ArrayList<>();
        result1.add(new Workstation());
        result2.add(new Transport());
        result3.add(new Stock());
        List<Location> result4 = new ArrayList<>(result1);
        result4.addAll(result2);
        result4.addAll(result3);
        when(workstationRepository.findAll()).thenReturn(result1);
        when(transportRepository.findAll()).thenReturn(result2);
        when(stockRepository.findAll()).thenReturn(result3);
        assertEquals(locationService.getAll(), result4);
    }

    /**
     * Test get all stock and workstations.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetAllStockAndWorkstations() throws FindByException {
        List<Workstation> result1 = new ArrayList<>();
        List<Stock> result3 = new ArrayList<>();
        result1.add(new Workstation());
        result3.add(new Stock());
        List<Location> result4 = new ArrayList<>(result1);
        result4.addAll(result3);
        when(workstationRepository.findAll()).thenReturn(result1);
        when(stockRepository.findAll()).thenReturn(result3);
        assertEquals(locationService.getAllStocksAndWorkstations(), result4);
    }

    /**
     * Test get by id for Stock.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdStock() throws InvalidIdException {
        Stock res = new Stock();
        when(stockRepository.findBy("id")).thenReturn(res);
        assertEquals(locationService.getById("id"), res);
    }

    /**
     * Test get by id for Workstation.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdWs() throws InvalidIdException {
        Workstation res = new Workstation();
        when(stockRepository.findBy("id")).thenReturn(null);
        when(workstationRepository.findBy("id")).thenReturn(res);
        assertEquals(locationService.getById("id"), res);
    }

    /**
     * Test get by id for Transport.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdTrans() throws InvalidIdException {
        Transport res = new Transport();
        when(stockRepository.findBy("id")).thenReturn(null);
        when(workstationRepository.findBy("id")).thenReturn(null);
        when(transportRepository.findBy("id")).thenReturn(res);
        assertEquals(locationService.getById("id"), res);
    }

    /**
     * Test get all assemblies at.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetAllAssembliesAt() throws FindByException {

        List<Carrier> carriers = new ArrayList<>();
        Carrier carrier = new Carrier();
        carrier.setLocation(transport);
        carriers.add(carrier);
        List<Assembly> assemblyList = new ArrayList<>();
        Assembly assembly = new Assembly();
        assembly.setCarriers(carriers);
        assemblyList.add(assembly);
        when(assemblyService.getAll()).thenReturn(assemblyList);
        assertEquals(locationService.getAllAssembliesAt(transport), assemblyList);
    }

    /**
     * Test useless create.
     *
     * @throws CreationException the creation exception
     */
    @Test
    public void testCreate() throws CreationException {
        assertNull(locationService.create(new Transport()));
    }

    /**
     * Test useless update.
     *
     * @throws UpdateException the update exception
     */
    @Test
    public void testUpdate() throws UpdateException {
        assertNull(locationService.update(new Transport()));
    }

}