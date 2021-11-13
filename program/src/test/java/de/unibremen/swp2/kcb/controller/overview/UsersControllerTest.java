package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.LocaleController;
import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.UserService;
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
 * Test Class to Test {@link UsersController}
 *
 * @author Robin
 */
public class UsersControllerTest {

    /**
     * Injected instance of userController.
     */
    @InjectMocks
    UsersController usersController;

    /**
     * Mocked version of userService entity.
     */
    @Mock
    UserService userService;

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
        usersController.init();
        List<User> empty = new ArrayList<>();
        when(userService.getAll()).thenReturn(empty);
        verify(userService, times(1)).getAll();
    }

    /**
     * Test refresh.
     */
    @Test
    public void testRefresh() {
        List<User> user = new ArrayList<>();
        user.add(new User());
        when(userService.getAll()).thenReturn(user);
        usersController.refresh();
        verify(userService, times(1)).getAll();
        assertEquals(usersController.getEntities(), user);
    }

    /**
     * Test refresh fail.
     */
    @Test
    public void testRefreshFail() {
        when(userService.getAll()).thenReturn(null);
        usersController.refresh();
        verify(userService, times(1)).getAll();
        List<User> empty = new ArrayList<>();
        assertEquals(usersController.getEntities(), empty);
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        User dummy = new User();
        dummy.setId("111");
        when(userService.getById("111")).thenReturn(dummy);
        assertEquals(usersController.getById("111"), dummy);
        verify(userService, times(1)).getById(anyString());
    }

    /**
     * Tests getById() in case nothing was found.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByIdFail() throws InvalidIdException {
        when(userService.getById("123")).thenThrow(new InvalidIdException("fail"));
        assertNull(usersController.getById("123"));
        verify(userService, times(1)).getById(anyString());
    }

    /**
     * Test get by username.
     */
    @Test
    public void testGetByUsername() {
        User user = new User();
        when(userService.getByUsername("username")).thenReturn(user);
        assertEquals(usersController.getByUserName("username"), user);
        verify(userService, times(1)).getByUsername(anyString());
    }

    /**
     * Test get by username fail.
     */
    @Test
    public void testGetByUsernameFail() {
        User user = new User();
        when(userService.getByUsername(null)).thenReturn(user);
        assertEquals(usersController.getByUserName(null), user);
        verify(userService, times(1)).getByUsername(null);
    }

    /**
     * Test get by email.
     */
    @Test
    public void testGetByEmail() {
        User user = new User();
        user.setEmail("email@mail.com");
        when(userService.getByEmail("email@mail.com")).thenReturn(user);
        assertEquals(usersController.getByEmail("email@mail.com"), user);
        verify(userService, times(1)).getByEmail("email@mail.com");
    }

    /**
     * Test get by email fail.
     */
    @Test
    public void testGetByEmailFail() {
        User user = new User();
        when(userService.getByEmail(null)).thenReturn(user);
        assertEquals(usersController.getByEmail(null), user);
        verify(userService, times(1)).getByEmail(null);
    }

    /**
     * Test get by last name.
     */
    @Test
    public void testGetByLastName() {
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setLastName("Lastname");
        list.add(user);
        when(userService.getByLastName("Lastname")).thenReturn(list);
        assertEquals(usersController.getByLastName("Lastname"), list);
        verify(userService, times(1)).getByLastName("Lastname");
    }

    /**
     * Test get by last name fail.
     */
    @Test
    public void testGetByLastNameFail() {
        List<User> empty = new ArrayList<>();
        when(userService.getByLastName(null)).thenReturn(empty);
        assertEquals(usersController.getByLastName(null), empty);
        verify(userService, times(1)).getByLastName(null);
    }

    /**
     * Test get by first name.
     */
    @Test
    public void testGetByFirstName() {
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setFirstName("Firstname");
        list.add(user);
        when(userService.getByFirstName("Firstname")).thenReturn(list);
        assertEquals(usersController.getByFirstName("Firstname"), list);
        verify(userService, times(1)).getByFirstName("Firstname");
    }

    /**
     * Test get by first name fail.
     */
    @Test
    public void testGetByFirstNameFail() {
        List<User> empty = new ArrayList<>();
        when(userService.getByFirstName(null)).thenReturn(empty);
        assertEquals(usersController.getByFirstName(null), empty);
        verify(userService, times(1)).getByFirstName(null);
    }

    /**
     * Test role to badge.
     */
    @Test
    public void testRoleToBadge() {
        Role admin = Role.ADMIN;
        assertEquals(usersController.roleToBadge(admin), "bg-inverse-danger");
        Role pkp = Role.PKP;
        assertEquals(usersController.roleToBadge(pkp), "bg-inverse-success");
        Role transport = Role.TRANSPORT;
        assertEquals(usersController.roleToBadge(transport), "bg-inverse-warning");
        Role logistiker = Role.LOGISTIKER;
        assertEquals(usersController.roleToBadge(logistiker), "bg-inverse-primary");
        Role technologe = Role.TECHNOLOGE;
        assertEquals(usersController.roleToBadge(technologe), "bg-inverse-info");
    }
}