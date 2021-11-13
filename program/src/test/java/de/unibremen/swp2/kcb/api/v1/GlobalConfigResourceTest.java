package de.unibremen.swp2.kcb.api.v1;

import de.unibremen.swp2.kcb.model.GlobalConfig;
import de.unibremen.swp2.kcb.service.GlobalConfigService;
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
 * The type Global config resource test.
 *
 * @author Marius
 */
public class GlobalConfigResourceTest {

    /**
     * Injected instance of resource
     */
    @InjectMocks
    private GlobalConfigResource resource;

    /**
     * Mocked version of jsonUtil
     */
    @Mock
    private JsonUtil jsonUtil;

    /**
     * Mocked version of service
     */
    @Mock
    private GlobalConfigService service;

    /**
     * Mocked version of model
     */
    @Mock
    private GlobalConfig mockedGlobalConfig;

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
    @Test
    public void testCreateNormal() throws CreationException {
        when(service.create(any())).thenReturn(mockedGlobalConfig);
        when(jsonUtil.marshal(any())).thenReturn("");
        resource.create(mockedGlobalConfig);
        verify(service, times(1)).create(mockedGlobalConfig);
    }

    /**
     * Test create fail.
     *
     * @throws CreationException the creation exception
     */
    @Test
    public void testCreateFail() throws CreationException {
        when(service.create(any())).thenThrow(CreationException.class);
        resource.create(mockedGlobalConfig);
        verify(service, times(1)).create(mockedGlobalConfig);
    }

    /**
     * Test create normal.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testUpdateNormal() throws UpdateException, InvalidIdException, FindByException {
        when(service.update(mockedGlobalConfig)).thenReturn(mockedGlobalConfig);
        when(service.getByKey(any())).thenReturn(mockedGlobalConfig);
        when(jsonUtil.marshal(any())).thenReturn("");
        resource.update(mockedGlobalConfig);
        verify(service, times(1)).update(mockedGlobalConfig);
    }

    /**
     * Test create normal.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testUpdateFail() throws UpdateException, InvalidIdException, FindByException {
        when(service.update(mockedGlobalConfig)).thenThrow(UpdateException.class);
        when(service.getByKey(any())).thenReturn(mockedGlobalConfig);
        when(jsonUtil.marshal(any())).thenReturn("");
        String response = resource.update(mockedGlobalConfig);
        verify(service, times(1)).update(mockedGlobalConfig);
    }

    /**
     * Test delete normal from id.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws DeletionException  the deletion exception
     */
    @Test
    public void testDeleteNormal() throws InvalidIdException, DeletionException, FindByException {
        final String body = "{'id': '1'}";
        when(service.getByKey(any())).thenReturn(mockedGlobalConfig);
        String response = resource.delete(body);
        verify(service, times(1)).delete(any());
        assertTrue("Respone should contain successfully.", response.contains("successfully"));
    }

    /**
     * Test delete normal from id.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws DeletionException  the deletion exception
     */
    @Test
    public void testDeleteInvalidId() {
        final String body = "{'dasd': '1'}";
        String response = resource.delete(body);
        assertTrue("Respone should contain invalid id.", response.contains("Invalid id"));
    }

    /**
     * Test delete failed.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws DeletionException  the deletion exception
     */
    @Test
    public void testDeleteFailed() throws InvalidIdException, DeletionException {
        final String body = "{'id': '1'}";
        doThrow(DeletionException.class).when(service).delete(any());
        String response = resource.delete(body);
        verify(service, times(1)).delete(any());
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException, FindByException {
        when(service.getByKey(any())).thenReturn(mockedGlobalConfig);
        String response = resource.getById("test");
        verify(jsonUtil, times(1)).marshal(any());
        verify(service, times(1)).getByKey("test");
    }

    /**
     * Test get by id fail.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException, FindByException {
        when(service.getByKey(any())).thenReturn(null);
        String response = resource.getById("test");
        verify(service, times(1)).getByKey("test");
        assertTrue("Response should contain not found.", response.contains("not found"));
    }

    /**
     * Test get by id fail.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFailException() throws FindByException {
        when(service.getByKey(any())).thenThrow(FindByException.class);
        String response = resource.getById("test");
        verify(service, times(1)).getByKey("test");
        assertTrue("Response should contain invalid id.", response.contains("Invalid id"));
    }
}
