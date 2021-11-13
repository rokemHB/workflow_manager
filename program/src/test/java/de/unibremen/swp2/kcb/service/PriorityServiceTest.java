package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.PriorityRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.PriorityValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * The type Priority service test.
 *
 * @author Marc
 * @author Robin
 */
public class PriorityServiceTest {

    /**
     * Instance of {@link PriorityService} that gets all mocked instances injected.
     */
    @InjectMocks
    private PriorityService priorityService;

    /**
     * Injected instance of {@link PriorityRepository}
     */
    @Mock
    private PriorityRepository priorityRepository;

    /**
     * Injected instance of {@link UserService}
     */
    @Mock
    private UserService userService;

    /**
     * Injected instance of {@link PriorityValidator}
     */
    @Mock
    private PriorityValidator priorityValidator;

    /**
     * Sets up tests.
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
        List<Priority> priorityList = new ArrayList<>();
        Priority a = new Priority();
        a.setName("PrioA");
        Priority b = new Priority();
        a.setName("PrioB");
        priorityList.add(a);
        priorityList.add(b);

        when(priorityRepository.findAll()).thenReturn(priorityList);

        assertEquals(priorityService.getAll().size(), 2);
        assertEquals(priorityList.get(0), a);
        assertEquals(priorityList.get(1), b);
    }

    /**
     * Test get by name fail.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByNameFail() throws FindByException {
        priorityService.getByName("");
    }

    /**
     * Test get by name not found.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByNameNotFound() throws FindByException {
        when(priorityRepository.findByName(anyString())).thenReturn(null);
        priorityService.getByName("LEL");
    }

    /**
     * Test get by name.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetByName() throws FindByException {
        Priority a = new Priority();
        a.setName("PrioA");
        when(priorityRepository.findByName(a.getName())).thenReturn(a);

        assertEquals(priorityService.getByName("PrioA"), a);
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetByID() throws InvalidIdException {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);
        User u = new User();
        u.setUsername("Dood");

        when(priorityRepository.findBy(a.getId())).thenReturn(a);
        when(userService.getExecutingUser()).thenReturn(u);

        assertEquals(priorityService.getById(a.getId()), a);
    }

    /**
     * Test get by id with invalid id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdwithInvalidID() throws InvalidIdException {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);
        a.setId(null);
        User u = new User();
        u.setUsername("Dood");

        when(priorityRepository.findBy(a.getId())).thenReturn(a);
        when(userService.getExecutingUser()).thenReturn(u);

        assertEquals(priorityService.getById(a.getId()), a);
    }

    /**
     * Test create success.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateSuccess() throws ValidationException, CreationException, EntityAlreadyExistingException {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);
        User u = new User();
        u.setUsername("Dood");

        when(priorityValidator.validate(any())).thenReturn(true);
        when(priorityRepository.save(any())).thenReturn(a);
        when(userService.getExecutingUser()).thenReturn(u);
        when(priorityRepository.findByName(any())).thenThrow(NoResultException.class); 

        assertEquals(priorityService.create(a), a);
    }

    /**
     * Test create validation fail.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFail() throws ValidationException, CreationException, EntityAlreadyExistingException {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);

        when(priorityValidator.validate(any())).thenReturn(false);

        priorityService.create(a);
    }

    /**
     * Test update.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test
    public void testUpdate() throws ValidationException, UpdateException {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);
        User u = new User();
        u.setUsername("Dood");

        when(priorityValidator.validate(any())).thenReturn(true);
        when(priorityRepository.findBy(a.getId())).thenReturn(a);
        when(priorityRepository.saveAndFlushAndRefresh(a)).thenReturn(a);
        when(userService.getExecutingUser()).thenReturn(u);

        priorityService.update(a);
    }

    /**
     * Test update validation fail.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationFail() throws ValidationException, UpdateException {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);
        User u = new User();
        u.setUsername("Dood");

        when(priorityValidator.validate(any())).thenReturn(false);
        when(priorityRepository.findBy(a.getId())).thenReturn(null);
        when(userService.getExecutingUser()).thenReturn(u);

        priorityService.update(a);
    }

    /**
     * Test update return null.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateReturnNull() throws ValidationException, UpdateException {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);
        User u = new User();
        u.setUsername("Dood");

        when(priorityValidator.validate(any())).thenReturn(true);
        when(priorityRepository.findBy(a.getId())).thenReturn(null);
        when(userService.getExecutingUser()).thenReturn(u);

        priorityService.update(a);
    }

    /**
     * Test delete.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDelete() throws DeletionException {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);
        User u = new User();
        u.setUsername("Dood");

        when(priorityRepository.findBy(a.getId())).thenReturn(a);
        when(userService.getExecutingUser()).thenReturn(u);

        priorityService.delete(a);

        verify(priorityRepository, times(1)).attachAndRemove(any());
    }

    /**
     * Test delete not found.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteNotFound() throws DeletionException {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);
        User u = new User();
        u.setUsername("Dood");

        when(priorityRepository.findBy(a.getId())).thenReturn(null);
        when(userService.getExecutingUser()).thenReturn(u);

        priorityService.delete(a);
    }

    /**
     * Test get color.
     */
    @Test
    public void testGetColor() {
        Priority a = new Priority();
        a.setName("PrioA");
        a.setValue(10);
        Priority b = new Priority();
        b.setName("PrioB");
        b.setValue(100);
        Priority c = new Priority();
        c.setName("PrioC");
        c.setValue(50);

        List<Priority> priorities = new ArrayList<>();
        priorities.add(a);
        priorities.add(b);

        when(priorityRepository.findAllOrderByValueAsc()).thenReturn(priorities);

        String color = priorityService.getColor(b);
        assertEquals(color, "rgb(255.0, 1, 100)");

        color = priorityService.getColor(a);
        assertEquals(color, "rgb(0.0, 255, 100)");

        color = priorityService.getColor(c);
        assertEquals(color, "rgb(0.0, 255, 100)");
    }

}