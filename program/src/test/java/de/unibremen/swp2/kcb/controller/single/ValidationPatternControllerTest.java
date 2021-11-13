package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.service.ValidationPatternService;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class to test {@link ValidationPatternController}.
 *
 * @author Robin
 */
public class ValidationPatternControllerTest {

    /**
     * Mocked version of LocaleController.
     */
    @Mock
    LocaleController localeController;

    /**
     * Carrier controller to be testet
     */
    @InjectMocks
    private ValidationPatternController validationPatternController;

    /**
     * Mocked version of carrierService
     */
    @Mock
    private ValidationPatternService validationPatternService;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests, if create calls the service correctly.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateNormal() {
        validationPatternController.create();
    }

    /**
     * Tests, if delete calls the service correctly.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDelet() {
        validationPatternController.delete();
    }

    /**
     * Tests, if update calls the service correctly.
     *
     * @throws UpdateException if updating fails
     */
    @Test
    public void testUpdateNormal() throws UpdateException {
        validationPatternController.update();
        verify(validationPatternService, times(1)).update(any());
    }

    /**
     * Tests, if update fails, when UpdateException occurs.
     *
     * @throws UpdateException if updating fails
     */
    @Test
    public void testUpdateFail() throws UpdateException {
        doThrow(UpdateException.class).when(validationPatternService).update(any());
        validationPatternController.update();
        verify(validationPatternService, times(1)).update(any());
    }

    /**
     * Test update fail illegal arg exception.
     *
     * @throws UpdateException the update exception
     */
    @Test
    public void testUpdateFailIllegalArg() throws UpdateException {
        doThrow(IllegalArgumentException.class).when(validationPatternService).update(any());
        validationPatternController.update();
        verify(validationPatternService, times(1)).update(any());
    }

}