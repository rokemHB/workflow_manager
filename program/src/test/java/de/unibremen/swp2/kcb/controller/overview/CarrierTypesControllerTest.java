package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.service.CarrierTypeService;
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
 * Test Class to Test {@link CarrierTypesController}.
 *
 * @author Robin
 */
public class CarrierTypesControllerTest {

    /**
     * Injected instance of carrierTypesController.
     */
    @InjectMocks
    CarrierTypesController carrierTypesController;

    /**
     * Mocked version of processStepService entity.
     */
    @Mock
    CarrierTypeService carrierTypeService;

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
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testInit() throws FindByException {
        carrierTypesController.init();
        List<CarrierType> empty = new ArrayList<>();
        when(carrierTypeService.getAll()).thenReturn(empty);
        verify(carrierTypeService, times(1)).getAll();
    }

    /**
     * Test refresh.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testRefresh() throws FindByException {
        List<CarrierType> assemblies = new ArrayList<>();
        assemblies.add(new CarrierType());
        when(carrierTypeService.getAll()).thenReturn(assemblies);
        carrierTypesController.refresh();
        verify(carrierTypeService, times(1)).getAll();
        assertEquals(carrierTypesController.getEntities(), assemblies);
    }

    /**
     * Test refresh fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testRefreshFail() throws FindByException {
        doThrow(FindByException.class).when(carrierTypeService).getAll();
        carrierTypesController.refresh();
        verify(carrierTypeService, times(1)).getAll();
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        CarrierType dummy = new CarrierType();
        dummy.setId("111");
        when(carrierTypeService.getById("111")).thenReturn(dummy);
        assertEquals(carrierTypesController.getById("111"), dummy);
        verify(carrierTypeService, times(1)).getById(anyString());
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(carrierTypeService.getById("123")).thenThrow(new InvalidIdException("fail"));
        assertNull(carrierTypesController.getById("123"));
        verify(carrierTypeService, times(1)).getById(anyString());
    }

    /**
     * Test get by name.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByName() throws FindByException {
        CarrierType dummy = new CarrierType();
        dummy.setName("name");
        when(carrierTypeService.getByName("name")).thenReturn(dummy);
        assertEquals(carrierTypesController.getByName("name"), dummy);
        verify(carrierTypeService, times(1)).getByName(anyString());
    }

    /**
     * Test get by name fail.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByNameFail() throws FindByException {
        when(carrierTypeService.getByName(anyString())).thenThrow(FindByException.class);
        assertNull(carrierTypesController.getByName(anyString()));
    }


}