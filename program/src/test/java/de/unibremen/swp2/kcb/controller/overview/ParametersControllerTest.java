package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.service.ParameterService;
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
 * Test class to test {@link ParametersController}
 *
 * @author Robin
 */
public class ParametersControllerTest {

    /**
     * Injected instance of assembliesController.
     */
    @InjectMocks
    ParametersController parametersController;

    /**
     * Mocked version of assemblyService entity.
     */
    @Mock
    ParameterService parameterService;

    /**
     * The Locale controller.
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
        parametersController.init();
        List<Parameter> empty = new ArrayList<>();
        when(parameterService.getAll()).thenReturn(empty);
        verify(parameterService, times(1)).getAll();
    }

    /**
     * Test for the refresh() Methode of PrioritiesController.
     */
    @Test
    public void testRefresh() {
        List<Parameter> prios = new ArrayList<>();
        prios.add(new Parameter());
        when(parameterService.getAll()).thenReturn(prios);
        parametersController.refresh();
        verify(parameterService, times(1)).getAll();
        assertEquals(parametersController.getEntities(), prios);
    }

    /**
     * Test refresh fail.
     */
    @Test
    public void testRefreshFail() {
        when(parameterService.getAll()).thenReturn(null);
        parametersController.refresh();
        verify(parameterService, times(1)).getAll();
        List<Priority> empty = new ArrayList<>();
        assertEquals(parametersController.getEntities(), empty);
    }

    /**
     * Tests the getById() method of parametersController.
     *
     * @throws InvalidIdException when failed.
     */
    @Test
    public void testGetById() throws InvalidIdException {
        Parameter dummy = new Parameter();
        dummy.setId("111");
        when(parameterService.getById("111")).thenReturn(dummy);
        assertEquals(parametersController.getById("111"), dummy);
        verify(parameterService, times(1)).getById(anyString());
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(parameterService.getById("123")).thenThrow(new InvalidIdException("fail"));
        assertNull(parametersController.getById("123"));
        verify(parameterService, times(1)).getById(anyString());
    }
}