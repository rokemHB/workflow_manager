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
 * Test class to test {@link ValidationPatternsController}
 *
 * @author Robin
 */
public class ValidationPatternsControllerTest {

    /**
     * Injected instance of validationPatternsController.
     */
    @InjectMocks
    ValidationPatternsController validationPatternsController;

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
        validationPatternsController.init();
        List<ValidationPattern> empty = new ArrayList<>();
        when(validationPatternService.getByAdvanced(false)).thenReturn(empty);
        verify(validationPatternService, times(1)).getByAdvanced(false);
    }

    /**
     * Test refresh.
     */
    @Test
    public void testRefresh() {
        List<ValidationPattern> res = new ArrayList<>();
        res.add(new ValidationPattern());
        when(validationPatternService.getByAdvanced(false)).thenReturn(res);
        validationPatternsController.refresh();
        verify(validationPatternService, times(1)).getByAdvanced(false);
        assertEquals(validationPatternsController.getEntities(), res);
    }

    /**
     * Test refresh fail.
     */
    @Test
    public void testRefreshFail() {
        when(validationPatternService.getByAdvanced(false)).thenReturn(null);
        validationPatternsController.refresh();
        verify(validationPatternService, times(1)).getByAdvanced(false);
        List<ValidationPattern> empty = new ArrayList<>();
        assertEquals(validationPatternsController.getEntities(), empty);
    }

    /**
     * Gets by id not needed.
     */
    @Test
    public void getByIdNotNeeded() {
        assertNull(validationPatternsController.getById("id"));
    }


}