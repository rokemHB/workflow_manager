package de.unibremen.swp2.kcb.api.v1;

import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The type User resource test.
 *
 * @author Marius
 */
public class UserResourceTest {

    /**
     * Injected instance of resource
     */
    @InjectMocks
    private UserResource resource;

    /**
     * Mocked version of jsonUtil
     */
    @Mock
    private JsonUtil jsonUtil;

    /**
     * Mocked version of service
     */
    @Mock
    private UserService service;

    /**
     * Mocked version of model
     */
    @Mock
    private User mockedUser;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
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
        when(service.create(any())).thenReturn(new User());
        when(jsonUtil.marshal(any())).thenReturn("");
        resource.create(mockedUser);
        verify(service, times(1)).create(mockedUser);
    }

    /**
     * Test create fail.
     *
     * @throws CreationException the creation exception
     */
    @Test
    public void testCreateFail() throws CreationException, EntityAlreadyExistingException {
        when(service.create(any())).thenThrow(CreationException.class);
        resource.create(mockedUser);
        verify(service, times(1)).create(mockedUser);
    }

    /**
     * Test create normal.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testUpdateNormal() throws UpdateException, InvalidIdException {
        when(service.update(mockedUser)).thenReturn(mockedUser);
        when(service.getById(any())).thenReturn(mockedUser);
        when(mockedUser.getPassword()).thenReturn("");
        when(jsonUtil.marshal(any())).thenReturn("");
        resource.update(mockedUser);
        verify(service, times(1)).update(mockedUser);
        verify(mockedUser, times(1)).setPassword("");
    }

    /**
     * Test create fail.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testUpdateFailInvalidID() throws UpdateException, InvalidIdException {
        when(service.getById(any())).thenThrow(InvalidIdException.class);
        when(mockedUser.getPassword()).thenReturn("");
        when(jsonUtil.marshal(any())).thenReturn("");
        String result = resource.update(mockedUser);
        verify(service, times(0)).update(mockedUser);
        verify(mockedUser, times(0)).setPassword(any());
        verify(mockedUser, times(0)).getPassword();
        assertTrue("Response message should contain invalid id.", result.contains("Invalid"));
    }

    /**
     * Test create fail.
     *
     * @throws UpdateException    the update exception
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testUpdateFailUpdateFailed() throws UpdateException, InvalidIdException {
        when(service.update(any())).thenThrow(UpdateException.class);
        when(service.getById(any())).thenReturn(mockedUser);
        when(mockedUser.getPassword()).thenReturn("");
        when(jsonUtil.marshal(any())).thenReturn("");
        final String result = resource.update(mockedUser);
        verify(service, times(1)).update(mockedUser);
        verify(mockedUser, times(1)).setPassword(any());
        verify(mockedUser, times(1)).getPassword();
        assertEquals("Response message should be empty if error message is null", "", result);
    }

    /**
     * Test delete normal from id.
     *
     * @throws InvalidIdException the invalid id exception
     * @throws DeletionException  the deletion exception
     */
    @Test
    public void testDeleteNormalFromID() throws InvalidIdException, DeletionException {
        final String body = "{'id': '1'}";
        final String mockedUserToStringMessage = "foobarusertostring";
        when(service.getById(any())).thenReturn(mockedUser);
        when(mockedUser.toString()).thenReturn(mockedUserToStringMessage);
        final String response = resource.delete(body);
        verify(service, times(1)).delete(mockedUser);
        assertTrue("Response should contain message: ", response.contains(mockedUserToStringMessage));
    }

    /**
     * Test delete normal from id.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDeleteNormalFromUsername() throws DeletionException {
        final String body = "{'username': 'foobar'}";
        final String mockedUserToStringMessage = "foobarusertostring";
        when(service.getByUsername(any())).thenReturn(mockedUser);
        when(mockedUser.toString()).thenReturn(mockedUserToStringMessage);
        final String response = resource.delete(body);
        verify(service, times(1)).delete(mockedUser);
        assertTrue("Response should contain message: ", response.contains(mockedUserToStringMessage));
    }

    /**
     * Test delete normal from id.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDeleteNormalFromEmail() throws DeletionException {
        final String body = "{'email': 'foo@bar.de'}";
        final String mockedUserToStringMessage = "foobarusertostring";
        when(service.getByEmail(any())).thenReturn(mockedUser);
        when(mockedUser.toString()).thenReturn(mockedUserToStringMessage);
        final String response = resource.delete(body);
        verify(service, times(1)).delete(mockedUser);
        assertTrue("Response should contain message: ", response.contains(mockedUserToStringMessage));
    }

    /**
     * Test delete normal from id.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDeleteFromIdInvalid() throws InvalidIdException {
        final String body = "{'id': 'foo@bar.de'}";
        when(service.getByEmail(any())).thenReturn(mockedUser);
        when(service.getById(any())).thenThrow(InvalidIdException.class);
        final String response = resource.delete(body);
        assertTrue("User not found should be in response if id is invalid.", response.contains("User not found"));
    }

    /**
     * Test delete fail.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDeleteFail() throws DeletionException {
        final String body = "{'email': 'foo@bar.de'}";
        doThrow(DeletionException.class).when(service).delete(mockedUser);
        when(service.getByEmail(any())).thenReturn(mockedUser);
        String response = resource.delete(body);
        verify(service, times(1)).delete(mockedUser);
        assertTrue("Response should contain message.", response.contains("failed"));
    }

    /**
     * Test delete invalid json.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDeleteInvalidJson() throws DeletionException {
        final String body = "{'k': 'foo'}";
        doThrow(DeletionException.class).when(service).delete(mockedUser);
        when(service.getByEmail(any())).thenReturn(mockedUser);
        String response = resource.delete(body);
        assertTrue("Response should contain message.", response.contains("not found"));
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        when(service.getById(any())).thenReturn(mockedUser);
        when(jsonUtil.marshal(any())).thenReturn("");
        String response = resource.getById("test");
        verify(service, times(1)).getById(any());
    }

    @Test
    public void testGetByIdNull() throws InvalidIdException {
        when(service.getById(any())).thenReturn(null);
        String response = resource.getById("test");
        verify(service, times(1)).getById(any());
        assertTrue("User not found should be in response", response.contains("User not found"));
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdInvalid() throws InvalidIdException {
        when(service.getById(any())).thenThrow(InvalidIdException.class);
        when(jsonUtil.marshal(any())).thenReturn("");
        String response = resource.getById("test");
        assertTrue("Invalid id should be in response", response.contains("Invalid id"));
    }
}
