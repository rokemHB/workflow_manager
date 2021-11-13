package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.controller.single.ContextMocker;
import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.service.AssemblyService;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.faces.context.FacesContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test Class to Test {@link AssemblyConverter}
 *
 * @author Robin
 */
public class AssemblyConverterTest {

    /**
     * Injected instance of assemblyConverter to be tested.
     */
    @InjectMocks
    private AssemblyConverter assemblyConverter;

    /**
     * Mocked version of assemblyService
     */
    @Mock
    private AssemblyService assemblyService;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test get as object.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetAsObject() throws InvalidIdException {
        Assembly assembly = new Assembly();
        when(assemblyService.getById(any())).thenReturn(assembly);
        FacesContext context = ContextMocker.mockFacesContext();
        assertEquals(assemblyConverter.getAsObject(context, any(), "string"), assembly);
    }
}