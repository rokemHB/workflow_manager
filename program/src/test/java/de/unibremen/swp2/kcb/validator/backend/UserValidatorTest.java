package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.validator.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Test Class to Test {@link UserValidator}
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class UserValidatorTest extends ValidatorTest {
    /**
     * UserValidator to be tested
     */
    @InjectMocks
    private UserValidator userValidator;

    /**
     * Setup test environment
     */
    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * Test Validator with an empty user
     */
    @Test(expected = ValidationException.class)
    public void testNullUser() throws ValidationException {
        userValidator.validate(null);
    }

    /**
     * Test Validator with valid user
     */
    @Test
    public void testValidUser() throws ValidationException {
        User user = new User();
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        user.setEmail("foo@bar.de");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setUsername("foo.bar");
        user.setPassword("12345678");
        user.setRolesFromRoles(roles);
        assertTrue(user.toString() + " should be valid.", userValidator.validate(user));
        verify(validatorConfig).getPattern("Email");
        verify(validatorConfig).getPattern("Username");
        verify(validatorConfig).getPattern("Name");
    }

    /**
     * Test Validator with user with null roles
     */
    @Test(expected = ValidationException.class)
    public void testUserNullRoles() throws ValidationException {
        User user = new User();
        user.setEmail("foo@bar.de");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setUsername("foo.bar");
        user.setPassword("12345678");
        user.setRoles(null);
        userValidator.validate(user);
    }

    /**
     * Test Validator with user with null email
     */
    @Test(expected = ValidationException.class)
    public void testUserNullEmail() throws ValidationException {
        User user = new User();
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        user.setEmail(null);
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setUsername("foo.bar");
        user.setPassword("12345678");
        user.setRolesFromRoles(roles);
        userValidator.validate(user);
    }

    /**
     * Test Validator with user with null first name
     */
    @Test(expected = ValidationException.class)
    public void testUserNullFirstName() throws ValidationException {
        User user = new User();
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        user.setEmail("foo@bar.de");
        user.setFirstName(null);
        user.setLastName("Bar");
        user.setUsername("foo.bar");
        user.setPassword("12345678");
        user.setRolesFromRoles(roles);
        userValidator.validate(user);
    }

    /**
     * Test Validator with user with null last name
     */
    @Test(expected = ValidationException.class)
    public void testUserNullLastName() throws ValidationException {
        User user = new User();
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        user.setEmail("foo@bar.de");
        user.setFirstName("Foo");
        user.setLastName(null);
        user.setUsername("foo.bar");
        user.setPassword("12345678");
        user.setRolesFromRoles(roles);
        userValidator.validate(user);
    }

    /**
     * Test Validator with user with null username
     */
    @Test(expected = ValidationException.class)
    public void testUserNullUsername() throws ValidationException {
        User user = new User();
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        user.setEmail("foo@bar.de");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setUsername(null);
        user.setPassword("12345678");
        user.setRolesFromRoles(roles);
        userValidator.validate(user);
    }

    /**
     * Test Validator with user with null password
     */
    @Test(expected = ValidationException.class)
    public void testUserNullPassword() throws ValidationException {
        User user = new User();
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        user.setEmail("foo@bar.de");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setUsername("foo.bar");
        user.setPassword(null);
        user.setRolesFromRoles(roles);
        userValidator.validate(user);
    }

    /**
     * Test Validator with user with empty roles
     */
    @Test
    public void testUserEmptyRoles() throws ValidationException {
        User user = new User();
        HashSet<Role> roles = new HashSet<>();
        user.setEmail("foo@bar.de");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setUsername("foo.bar");
        user.setPassword("12345678");
        user.setRolesFromRoles(roles);
        assertFalse("User with empty roles should be invalid.", userValidator.validate(user));
    }
}
