package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.model.parameter.CardinalValue;
import de.unibremen.swp2.kcb.persistence.parameter.CardinalValueRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import de.unibremen.swp2.kcb.validator.backend.ValueValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.PersistenceException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class to test {@link CardinalValueService}
 *
 * @author Robin
 * @author Arvid
 */
public class CardinalValueServiceTest {

    /**
     * Injected instance of cardinalValueService
     */
    @InjectMocks
    private CardinalValueService cardinalValueService;

    /**
     * Mocked version of cardinalValueRepository
     */
    @Mock
    private CardinalValueRepository cardinalValueRepository;

    /**
     * Mocked version of valueValidator
     */
    @Mock
    private ValueValidator valueValidator;

    /**
     * Mocked version of userService
     */
    @Mock
    private UserService userService;

    /**
     * Sets up tests.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test create.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test
    public void testCreate() throws ValidationException, CreationException {
        when(valueValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("HansWurscht");
        when(userService.getExecutingUser()).thenReturn(testUser);
        CardinalValue value = new CardinalValue();
        cardinalValueService.create(value);
        verify(cardinalValueRepository, times(1)).save(value);
    }

    /**
     * Test invalid create.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationException() throws ValidationException, CreationException {
        when(valueValidator.validate(any())).thenThrow(ValidationException.class);
        cardinalValueService.create(new CardinalValue());
    }

    /**
     * Test create invalid.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateInvalid() throws ValidationException, CreationException {
        when(valueValidator.validate(any())).thenReturn(false);
        cardinalValueService.create(new CardinalValue());
    }

    /**
     * Test create save in repo fail.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     */
    @Test(expected = CreationException.class)
    public void testCreateSaveInRepoFail() throws ValidationException, CreationException {
        CardinalValue testValue = new CardinalValue();
        doThrow(new PersistenceException("create failed")).when(cardinalValueRepository).save(testValue);
        when(valueValidator.validate(any())).thenReturn(true);
        cardinalValueService.create(testValue);
    }

    /**
     * Test update.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test
    public void testUpdate() throws ValidationException, UpdateException {
        when(valueValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("HansWurscht");
        when(userService.getExecutingUser()).thenReturn(testUser);
        CardinalValue value = new CardinalValue();
        when(cardinalValueRepository.findBy(any())).thenReturn(value);
        cardinalValueService.update(value);
        verify(cardinalValueRepository, times(1)).saveAndFlushAndRefresh(value);
    }

    /**
     * Test update validation exception.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationException() throws ValidationException, UpdateException {
        when(valueValidator.validate(any())).thenThrow(ValidationException.class);
        cardinalValueService.update(new CardinalValue());
    }

    /**
     * Test update invalid.
     *
     * @throws UpdateException the update exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateInvalid() throws UpdateException, ValidationException {
        when(valueValidator.validate(any())).thenReturn(false);
        cardinalValueService.update(new CardinalValue());
    }

    /**
     * Test update save in repo fail.
     *
     * @throws UpdateException the update exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateSaveInRepoFail() throws UpdateException, ValidationException {
        CardinalValue testValue = new CardinalValue();
        doThrow(new PersistenceException("create failed")).when(cardinalValueRepository).saveAndFlushAndRefresh(testValue);
        when(valueValidator.validate(any())).thenReturn(true);
        when(cardinalValueRepository.findBy(any())).thenReturn(new CardinalValue());
        cardinalValueService.update(testValue);
    }

    /**
     * Test update stored value fail.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateStoredValueFail() throws ValidationException, UpdateException {
        when(valueValidator.validate(any())).thenReturn(true);
        when(cardinalValueRepository.findBy(any())).thenReturn(null);
        cardinalValueService.update(new CardinalValue());
    }

    /**
     * Test delete.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDelete() throws DeletionException {
        User testUser = new User();
        testUser.setUsername("HansWurscht");
        when(userService.getExecutingUser()).thenReturn(testUser);
        when(cardinalValueRepository.findBy(any())).thenReturn(new CardinalValue());
        doNothing().when(cardinalValueRepository).attachAndRemove(any());
        cardinalValueService.delete(new CardinalValue());
        verify(cardinalValueRepository, times(1)).attachAndRemove(any());
    }

    /**
     * Test delete stored null.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteStoredNull() throws DeletionException {
        when(cardinalValueRepository.findBy(any())).thenReturn(null);
        cardinalValueService.delete(new CardinalValue());
    }

    /**
     * Test delete from repo fail.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteFromRepoFail() throws DeletionException {
        when(cardinalValueRepository.findBy(any())).thenReturn(new CardinalValue());
        doThrow(new PersistenceException("deleteFailed")).when(cardinalValueRepository).attachAndRemove(any());
        cardinalValueService.delete(new CardinalValue());
    }

}