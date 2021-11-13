package de.unibremen.swp2.kcb.api.v1;

import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.service.StateMachineService;
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
 * The type State machine resource test.
 *
 * @author Marius
 */
public class StateMachineResourceTest {

    /**
     * Injected instance of resource
     */
    @InjectMocks
    private StateMachineResource resource;

    /**
     * Mocked version of jsonUtil
     */
    @Mock
    private JsonUtil jsonUtil;

    /**
     * Mocked version of service
     */
    @Mock
    private StateMachineService service;

    /**
     * Mocked version of model
     */
    @Mock
    private StateMachine mockedStateMachine;

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
    public void testCreateNormal() throws CreationException, EntityAlreadyExistingException {
        when(service.create(any())).thenReturn(mockedStateMachine);
        when(jsonUtil.marshal(any())).thenReturn("");
        resource.create(mockedStateMachine);
        verify(service, times(1)).create(mockedStateMachine);
    }

    /**
     * Test create fail.
     *
     * @throws CreationException the creation exception
     */
    @Test
    public void testCreateFail() throws CreationException, EntityAlreadyExistingException {
        when(service.create(any())).thenThrow(CreationException.class);
        resource.create(mockedStateMachine);
        verify(service, times(1)).create(mockedStateMachine);
    }

    /**
     * Test create normal.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testUpdateNormal() throws UpdateException, InvalidIdException {
        when(service.update(mockedStateMachine)).thenReturn(mockedStateMachine);
        when(service.getById(any())).thenReturn(mockedStateMachine);
        when(jsonUtil.marshal(any())).thenReturn("");
        resource.update(mockedStateMachine);
        verify(service, times(1)).update(mockedStateMachine);
    }

    /**
     * Test create normal.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testUpdateFail() throws UpdateException, InvalidIdException {
        when(service.update(mockedStateMachine)).thenThrow(UpdateException.class);
        when(service.getById(any())).thenReturn(mockedStateMachine);
        when(jsonUtil.marshal(any())).thenReturn("");
        String response = resource.update(mockedStateMachine);
        verify(service, times(1)).update(mockedStateMachine);
    }

    /**
     * Test delete normal from id.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws DeletionException  the deletion exception
     */
    @Test
    public void testDeleteNormal() throws InvalidIdException, DeletionException {
        final String body = "{'id': '1'}";
        when(service.getById(any())).thenReturn(mockedStateMachine);
        String response = resource.delete(body);
        verify(service, times(1)).delete(any());
        assertTrue("Respone should contain successfully.", response.contains("successfully"));
    }

    /**
     * Test delete invalid id exception.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws DeletionException  the deletion exception
     */
    @Test
    public void testDeleteInvalidIdException() throws InvalidIdException, DeletionException {
        final String body = "{'id': '1'}";
        when(service.getById(any())).thenThrow(InvalidIdException.class);
        String response = resource.delete(body);
        verify(service, times(0)).delete(any());
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
    public void testGetById() throws InvalidIdException {
        when(service.getById(any())).thenReturn(mockedStateMachine);
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
