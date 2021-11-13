package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.ResetToken;
import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.ResetTokenRepository;
import de.unibremen.swp2.kcb.persistence.UserRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.util.EmailUtil;
import de.unibremen.swp2.kcb.util.EntityManagerProducer;
import de.unibremen.swp2.kcb.validator.backend.UserValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Class to test {@link UserService}.
 *
 * @author Marius
 * @author Arvid
 * @author Marc
 */
//@RunWith(PowerMockRunner.class)
public class UserServiceTest {

    /**
     * Mocked version of the repository the given service uses.
     */
    @Mock
    protected UserRepository userRepository;
    /**
     * Mocked version of the repository the given service uses.
     */
    @Mock
    protected ResetTokenRepository resetTokenRepository;
    /**
     * Mocked version of the validator used by the given service.
     */
    @Mock
    protected UserValidator userValidator;
    /**
     * Service to be tested.
     */
    @InjectMocks
    private UserService userService;
    /**
     * Mocked version of the EntityManagerProducer
     */
    @Mock
    private EntityManagerProducer entityManagerProducer;

    /**
     * Mocked version of the EntityManager
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Mocked version of the EmailUtil
     */
    @Mock
    private EmailUtil emailUtil;

    /**
     * Mocked version of the User entity
     */
    @Mock
    private User user;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(entityManagerProducer.getEntityManager()).thenReturn(entityManager);
        when(user.getFirstName()).thenReturn("John");
        when(user.getLastName()).thenReturn("Doe");
        when(user.getUsername()).thenReturn("TestGuy");
        when(user.getPassword()).thenReturn("geheim");
        when(user.getEmail()).thenReturn("myMail");
        when(user.getId()).thenReturn("123");
        userService.setUp();
        userService.tearDown();
    }

    /**
     * Test create.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = UnavailableSecurityManagerException.class)
    public void testCreate() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(userValidator.validate(user)).thenReturn(true);
        when(userRepository.findBy("123")).thenReturn(user);
        when(userRepository.saveAndFlushAndRefresh(user)).thenReturn(user);

        userService.create(user);
    }

    /**
     * Test create save fail.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateSaveFail() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(userValidator.validate(user)).thenReturn(true);
        when(userRepository.save(user)).thenThrow(PersistenceException.class);
        doNothing().when(user).setPassword(anyString());

        assertNull(userService.create(user));

        verify(user, times(1)).getPassword();
        verify(user, times(1)).setPassword(any());
        verify(userRepository, times(1)).save(user);
        verify(userValidator, times(1)).validate(user);
    }

    /**
     * Test create validation false.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFalse() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(userValidator.validate(user)).thenReturn(false);

        assertEquals(userService.create(user), user);
        verify(userValidator, times(1)).validate(user);
    }

    /**
     * Test create validation fails.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFails() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(userValidator.validate(user)).thenThrow(new ValidationException("Entity was null."));
        userService.create(user);
        verify(userValidator, times(1)).validate(user);
    }

    /**
     * Test update.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UnavailableSecurityManagerException.class)
    public void testUpdate() throws ValidationException, UpdateException {
        when(userValidator.validate(user)).thenReturn(true);
        when(userRepository.findBy("123")).thenReturn(user);
        when(userRepository.saveAndFlushAndRefresh(user)).thenReturn(user);

        userService.update(user);
    }

    /**
     * Test update validate false.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidateFalse() throws ValidationException, UpdateException {
        when(userValidator.validate(user)).thenReturn(false);
        when(userRepository.findBy("123")).thenReturn(user);
        when(userRepository.saveAndFlushAndRefresh(user)).thenReturn(user);

        userService.update(user);
    }

    /**
     * Test update validate fail.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidateFail() throws ValidationException, UpdateException {
        when(userValidator.validate(user)).thenThrow(ValidationException.class);
        when(userRepository.findBy("123")).thenReturn(user);
        when(userRepository.saveAndFlushAndRefresh(user)).thenReturn(user);

        userService.update(user);
    }

    /**
     * Test update stored is null.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateStoredIsNull() throws ValidationException, UpdateException {
        when(userValidator.validate(user)).thenReturn(true);
        when(userRepository.findBy("123")).thenReturn(null);
        when(userRepository.saveAndFlushAndRefresh(user)).thenReturn(user);

        userService.update(user);
    }

    /**
     * Test update save fail.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateSaveFail() throws ValidationException, UpdateException {
        when(userValidator.validate(user)).thenReturn(true);
        when(userRepository.findBy("123")).thenReturn(user);
        when(userRepository.saveAndFlushAndRefresh(user)).thenThrow(PersistenceException.class);

        userService.update(user);
    }

    /**
     * Test get by username.
     */
    @Test
    public void testGetByUsername() {
        List<User> users = mock(List.class);
        when(users.size()).thenReturn(5);
        when(users.get(0)).thenReturn(user);
        when(userRepository.findByUsername("TestGuy")).thenReturn(users);

        assertEquals(user, userService.getByUsername(user.getUsername()));
    }

    /**
     * Test get by username null.
     */
    @Test
    public void testGetByUsernameNull() {
        List<User> users = mock(List.class);
        when(users.size()).thenReturn(5);
        when(userRepository.findByUsername("TestGuy")).thenReturn(users);

        assertNull(userService.getByUsername(null));
    }

    /**
     * Test get by users size zero.
     */
    @Test
    public void testGetByUsersSizeZero() {
        List<User> users = mock(List.class);
        when(users.size()).thenReturn(0);
        when(userRepository.findByUsername("TestGuy")).thenReturn(users);

        assertNull(userService.getByUsername(user.getUsername()));
    }

    /**
     * Test get by users null.
     */
    @Test
    public void testGetByUsersNull() {
        List<User> users = mock(List.class);
        when(users.size()).thenReturn(0);
        when(userRepository.findByUsername(null)).thenReturn(users);

        assertNull(userService.getByUsername(user.getUsername()));
    }

    /**
     * Test get by email.
     */
    @Test
    public void testGetByEmail() {
        List<User> users = mock(List.class);
        List<User> tmp = new ArrayList<>();
        tmp.add(user);
        tmp.add(user);
        when(users.size()).thenReturn(5);
        when(userRepository.findByEmail(user.getEmail())).thenReturn((tmp));
        when(users.get(0)).thenReturn(user);

        assertEquals(userService.getByEmail(user.getEmail()), user);
        verify(userRepository, times(1)).findByEmail("myMail");
    }

    /**
     * Test get by email null.
     */
    @Test
    public void testGetByEmailNull() {
        List<User> users = mock(List.class);
        List<User> tmp = new ArrayList<>();
        tmp.add(user);
        when(users.size()).thenReturn(5);
        when(userRepository.findByEmail(user.getEmail())).thenReturn((tmp));
        when(users.get(0)).thenReturn(user);

        assertEquals(userService.getByEmail(null), null);
    }

    /**
     * Test get by email size zero.
     */
    @Test
    public void testGetByEmailSizeZero() {
        List<User> users = mock(List.class);
        List<User> tmp = new ArrayList<>();
        when(users.size()).thenReturn(0);
        when(userRepository.findByEmail(user.getEmail())).thenReturn((tmp));
        when(users.get(0)).thenReturn(user);


        assertEquals(userService.getByEmail(user.getEmail()), null);
        verify(userRepository, times(1)).findByEmail("myMail");
    }

    /**
     * Test get by role.
     */
    @Test
    public void testGetByRole() {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(Role.TECHNOLOGE);
        List<User> users = new ArrayList<>();
        users.add(user);

        when(user.getRoles()).thenReturn(roleSet);
        when(userRepository.findAll()).thenReturn(users);
        when(userService.getAll()).thenReturn(users);

        assertEquals(userService.getByRole(Role.TECHNOLOGE).get(0), user);
        verify(userRepository, times(1)).findAll();
    }

    /**
     * Test notify.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNotify() {
        userService.notify(null, null);
    }

    /**
     * Test send new account mail.
     *
     * @throws MessagingException the messaging exception
     */
    @Test
    public void testSendNewAccountMail() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailUtil.createMessage()).thenReturn(mimeMessage);

        doNothing().when(mimeMessage).setContent(any());
        userService.sendNewAccountMail(user);
        verify(emailUtil, times(1)).send(any());

    }

    /**
     * Test send new account mail fail.
     *
     * @throws MessagingException the messaging exception
     */
    @Test(expected = MessagingException.class)
    public void testSendNewAccountMailFail() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailUtil.createMessage()).thenReturn(mimeMessage);
        doThrow(MessagingException.class).when(emailUtil).send(any());

        doNothing().when(mimeMessage).setContent(any());
        userService.sendNewAccountMail(user);
    }

    /**
     * Test get by first name.
     */
    @Test
    public void testGetByFirstName() {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findByFirstName("John")).thenReturn(users);

        assertEquals(user, userService.getByFirstName(user.getFirstName()).get(0));
    }

    /**
     * Test get by first name null.
     */
    @Test
    public void testGetByFirstNameNull() {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findByFirstName("John")).thenReturn(users);

        assertNull(userService.getByFirstName(null));
    }

    /**
     * Test get by last name.
     */
    @Test
    public void testGetByLastName() {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findByLastName("Doe")).thenReturn(users);

        assertEquals(user, userService.getByLastName(user.getLastName()).get(0));
    }

    /**
     * Test get by last name null.
     */
    @Test
    public void testGetByLastNameNull() {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findByLastName("Doe")).thenReturn(users);

        assertNull(userService.getByLastName(null));
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = UnavailableSecurityManagerException.class)
    public void testGetById() throws InvalidIdException {
        when(userRepository.findBy("123")).thenReturn(user);

        userService.getById(user.getId());
    }

    /**
     * Test get by id fail.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdFail() throws InvalidIdException {
        when(userRepository.findBy("123")).thenThrow(PersistenceException.class);

        userService.getById(user.getId());
    }

    /**
     * Test get by ids.
     */
    @Test(expected = UnavailableSecurityManagerException.class)
    public void testGetByIds() {
        List<String> ids = new ArrayList<>();
        ids.add("123");
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findBy("123")).thenReturn(user);

        assertEquals(user, userService.getByIds(ids));
    }

    /**
     * Test get by ids null.
     */
    @Test
    public void testGetByIdsNull() {
        List<String> ids = null;
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findBy("123")).thenReturn(user);

        assertEquals(Collections.emptyList(), userService.getByIds(null));
    }

    /**
     * Test get by ids size zero.
     */
    @Test
    public void testGetByIdsSizeZero() {
        List<String> ids = new ArrayList<>();
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findBy("123")).thenReturn(user);

        assertEquals(Collections.emptyList(), userService.getByIds(ids));
    }

    /**
     * Test get by ids null id.
     */
    @Test
    public void testGetByIdsNullID() {
        List<String> ids = new ArrayList<>();
        ids.add(null);
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findBy("123")).thenReturn(user);

        assertEquals(Collections.emptyList(), userService.getByIds(ids));
    }

    /**
     * Test reset password.
     *
     * @throws PasswordResetException the password reset exception
     * @throws MessagingException the messaging exception
     */
    @Test
    public void testResetPassword() throws PasswordResetException, MessagingException {
        List<User> users = new ArrayList<>();
        users.add(user);
        List<ResetToken> resetTokens = new ArrayList<>();

        when(userRepository.findByEmail("myMail")).thenReturn(users);
        when(resetTokenRepository.findByUser(user)).thenReturn(resetTokens);

        userService.resetPassword("myMail");
        verify(resetTokenRepository, times(1)).findByUser(user);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(emailUtil, times(1)).sendResetMessage(any(), any());
    }

    /**
     * Test reset password user null.
     *
     * @throws PasswordResetException the password reset exception
     * @throws MessagingException the messaging exception
     */
    @Test(expected = PasswordResetException.class)
    public void testResetPasswordUserNull() throws PasswordResetException, MessagingException {
        List<User> users = new ArrayList<>();
        users.add(user);
        List<ResetToken> resetTokens = new ArrayList<>();

        when(userRepository.findByEmail("myMail")).thenReturn(null);
        when(resetTokenRepository.findByUser(user)).thenReturn(resetTokens);

        userService.resetPassword("myMail");
        verify(resetTokenRepository, times(1)).findByUser(user);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(emailUtil, times(1)).sendResetMessage(any(), any());
    }

    /**
     * Test reset password send fail.
     *
     * @throws PasswordResetException the password reset exception
     * @throws MessagingException the messaging exception
     */
    @Test(expected = PasswordResetException.class)
    public void testResetPasswordSendFail() throws PasswordResetException, MessagingException {
        List<User> users = new ArrayList<>();
        users.add(user);
        List<ResetToken> resetTokens = new ArrayList<>();

        when(userRepository.findByEmail("myMail")).thenReturn(users);
        when(resetTokenRepository.findByUser(user)).thenReturn(resetTokens);
        doThrow(MessagingException.class).when(emailUtil).sendResetMessage(any(), any());

        userService.resetPassword("myMail");
        verify(resetTokenRepository, times(1)).findByUser(user);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(emailUtil, times(1)).sendResetMessage(any(), any());
    }

    /**
     * Test validate token invalid.
     *
     * @throws InvalidTokenException the invalid token exception
     */
    @Test(expected = InvalidTokenException.class)
    public void testValidateTokenInvalid() throws InvalidTokenException {
        List<ResetToken> resetTokens = new ArrayList<>();
        ResetToken resetToken = mock(ResetToken.class, RETURNS_DEEP_STUBS);
        resetTokens.add(resetToken);
        LocalTime localTime = LocalTime.now();

        when(resetTokenRepository.findByToken(anyString())).thenReturn(resetTokens);
        when(resetToken.getExpirationDate()).thenReturn(localTime);

        userService.validateToken(null);
    }

    /**
     * Test validate token stored invalid.
     *
     * @throws InvalidTokenException the invalid token exception
     */
    @Test(expected = InvalidTokenException.class)
    public void testValidateTokenStoredInvalid() throws InvalidTokenException {
        List<ResetToken> resetTokens = new ArrayList<>();
        ResetToken resetToken = mock(ResetToken.class, RETURNS_DEEP_STUBS);
        resetTokens.add(resetToken);
        LocalTime localTime = LocalTime.now();

        when(resetTokenRepository.findByToken(anyString())).thenReturn(null);
        when(resetToken.getExpirationDate()).thenReturn(localTime);

        userService.validateToken("999");
    }

    /**
     * Test confirm password change diff passwords.
     *
     * @throws InvalidTokenException the invalid token exception
     * @throws PasswordChangeException the password change exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = PasswordsNotMatchingException.class)
    public void testConfirmPasswordChangeDiffPasswords() throws InvalidTokenException, PasswordChangeException, ValidationException {
        List<ResetToken> resetTokens = new ArrayList<>();
        ResetToken resetToken = mock(ResetToken.class, RETURNS_DEEP_STUBS);
        resetTokens.add(resetToken);
        LocalTime localTime = LocalTime.now().plusMinutes(100);

        when(resetTokenRepository.findByToken(anyString())).thenReturn(resetTokens);
        when(resetToken.getExpirationDate()).thenReturn(localTime);
        when(resetToken.getUser()).thenReturn(user);
        when(userValidator.validate(user)).thenReturn(true);
        when(userRepository.findBy(user.getId())).thenReturn(user);

        userService.confirmPasswordChange("123", "1234", "999");
    }

    /**
     * Test confirm password change invalid token.
     *
     * @throws InvalidTokenException the invalid token exception
     * @throws PasswordChangeException the password change exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = InvalidTokenException.class)
    public void testConfirmPasswordChangeInvalidToken() throws InvalidTokenException, PasswordChangeException, ValidationException {
        List<ResetToken> resetTokens = new ArrayList<>();
        ResetToken resetToken = mock(ResetToken.class, RETURNS_DEEP_STUBS);
        resetTokens.add(resetToken);
        LocalTime localTime = LocalTime.now().plusMinutes(100);

        when(resetTokenRepository.findByToken(anyString())).thenReturn(resetTokens);
        when(resetToken.getExpirationDate()).thenReturn(localTime);
        when(resetToken.getUser()).thenReturn(user);
        when(userValidator.validate(user)).thenReturn(true);
        when(userRepository.findBy(user.getId())).thenReturn(user);

        userService.confirmPasswordChange("123", "123", null);
    }

    /**
     * Test toggle menu.
     */
    @Test
    public void testToggleMenu() {
        doNothing().when(user).setPinned(anyBoolean());
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.toggleMenu(user));
    }
}
