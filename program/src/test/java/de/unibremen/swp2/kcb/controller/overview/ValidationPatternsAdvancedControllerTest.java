package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.ValidationPattern;
import de.unibremen.swp2.kcb.service.ValidationPatternService;
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
 * Test class to test {@link ValidationPatternsAdvancedController}
 *
 * @author Robin
 */
public class ValidationPatternsAdvancedControllerTest {

    /**
     * Injected instance of validationPatternsController.
     */
    @InjectMocks
    ValidationPatternsAdvancedController validationPatternsAdvancedController;

    /**
     * Mocked version of validationPatternService entity.
     */
    @Mock
    ValidationPatternService validationPatternService;

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
        validationPatternsAdvancedController.init();
        List<ValidationPattern> empty = new ArrayList<>();
        when(validationPatternService.getByAdvanced(true)).thenReturn(empty);
        verify(validationPatternService, times(1)).getByAdvanced(true);
    }

    /**
     * Test refresh.
     */
    @Test
    public void testRefresh() {
        List<ValidationPattern> res = new ArrayList<>();
        res.add(new ValidationPattern());
        when(validationPatternService.getByAdvanced(true)).thenReturn(res);
        validationPatternsAdvancedController.refresh();
        verify(validationPatternService, times(1)).getByAdvanced(true);
        assertEquals(validationPatternsAdvancedController.getEntities(), res);
    }

    /**
     * Test refresh fail.
     */
    @Test
    public void testRefreshFail() {
        when(validationPatternService.getByAdvanced(true)).thenReturn(null);
        validationPatternsAdvancedController.refresh();
        verify(validationPatternService, times(1)).getByAdvanced(true);
        List<ValidationPattern> empty = new ArrayList<>();
        assertEquals(validationPatternsAdvancedController.getEntities(), empty);
    }

    /**
     * Gets by id not needed.
     */
    @Test
    public void getByIdNotNeeded() {
        assertNull(validationPatternsAdvancedController.getById("id"));
    }

}