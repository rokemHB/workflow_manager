package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.service.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.mockito.Mockito.*;

/**
 * Testclass to test RolesController class
 *
 * @author Robin
 */
public class RolesControllerTest {

    /**
     * Injected instance of rolesController.
     */
    @InjectMocks
    RolesController rolesController;

    /**
     * Mocked verison of roleService.
     */
    @Mock
    RoleService roleService;

    /**
     * SetUp Method to inject Mock-Objects.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test Init. Implicitly
     */
    @Test
    public void init() {
        rolesController.init();
        HashSet<Role> empty = new HashSet<>();
        when(roleService.getAll()).thenReturn(empty);
        verify(roleService, times(1)).getAll();
    }

}
