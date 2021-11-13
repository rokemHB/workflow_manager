package de.unibremen.swp2.kcb.api.v1;

import de.unibremen.swp2.kcb.model.ValidationPattern;
import de.unibremen.swp2.kcb.service.ValidationPatternService;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The type Validation pattern resource test.
 *
 * @author Marius
 */
public class ValidationPatternResourceTest {

    /**
     * Injected instance of resource
     */
    @InjectMocks
    private ValidationPatternResource resource;

    /**
     * Mocked version of jsonUtil
     */
    @Mock
    private JsonUtil jsonUtil;

    /**
     * Mocked version of service
     */
    @Mock
    private ValidationPatternService service;

    /**
     * Mocked version of model
     */
    @Mock
    private ValidationPattern mockedValidationPattern;

    /**
     * Sets up.
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
        when(service.getAll()).thenReturn(new ArrayList<>());
        when(jsonUtil.marshal(any())).thenReturn("");
        resource.getAll();
        verify(service, times(1)).getAll();
    }

    /**
     * Test create normal.
     *
     * @throws CreationException the creation exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateNormal() throws CreationException {
        resource.create(mockedValidationPattern);
    }

    /**
     * Test create normal.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testUpdateNormal() throws UpdateException, InvalidIdException {
        when(service.update(mockedValidationPattern)).thenReturn(mockedValidationPattern);
        when(jsonUtil.marshal(any())).thenReturn("");
        resource.update(mockedValidationPattern);
        verify(service, times(1)).update(mockedValidationPattern);
    }

    /**
     * Test create normal.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testUpdateFail() throws UpdateException, InvalidIdException {
        when(service.update(mockedValidationPattern)).thenThrow(UpdateException.class);
        when(jsonUtil.marshal(any())).thenReturn("");
        String response = resource.update(mockedValidationPattern);
        verify(service, times(1)).update(mockedValidationPattern);
    }

    /**
     * Test delete normal from id.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws DeletionException  the deletion exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteNormal() throws InvalidIdException, DeletionException {
        resource.delete(null);
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        when(service.getByName(any())).thenReturn(mockedValidationPattern);
        String response = resource.getById("test");
        verify(jsonUtil, times(1)).marshal(any());
        verify(service, times(1)).getByName("test");
    }

    /**
     * Test get by invalid id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByInvalidId() throws InvalidIdException {
        when(service.getByName(any())).thenReturn(null);
        String response = resource.getById("test");
        assertTrue("Response should contain not found.", response.contains("not found"));
    }
}
