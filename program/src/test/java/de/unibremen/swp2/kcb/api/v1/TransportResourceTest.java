package de.unibremen.swp2.kcb.api.v1;

import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.service.TransportService;
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
 * The type Transport resource test.
 *
 * @author Marius
 */
public class TransportResourceTest {

    /**
     * Injected instance of resource
     */
    @InjectMocks
    private TransportResource resource;

    /**
     * Mocked version of jsonUtil
     */
    @Mock
    private JsonUtil jsonUtil;

    /**
     * Mocked version of service
     */
    @Mock
    private TransportService service;

    /**
     * Mocked version of model
     */
    @Mock
    private Transport mockedTransport;

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
        resource.create(mockedTransport);
    }

    /**
     * Test create normal.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUpdateNormal() throws UpdateException, InvalidIdException {
        resource.update(mockedTransport);
    }

    /**
     * Test delete normal from id.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws DeletionException  the deletion exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteNormal() throws InvalidIdException, DeletionException {
        resource.delete("");
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        when(service.getById(any())).thenReturn(mockedTransport);
        String response = resource.getById("test");
        verify(jsonUtil, times(1)).marshal(any());
        verify(service, times(1)).getById("test");
    }

    /**
     * Test get by id fail.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(service.getById(any())).thenReturn(null);
        String response = resource.getById("test");
        verify(service, times(1)).getById("test");
        assertTrue("Response should contain not found.", response.contains("not found"));
    }

    /**
     * Test get by invalid id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByInvalidId() throws InvalidIdException {
        when(service.getById(any())).thenThrow(InvalidIdException.class);
        String response = resource.getById("test");
        verify(service, times(1)).getById("test");
        assertTrue("Response should contain invalid id.", response.contains("Invalid id"));
    }
}
