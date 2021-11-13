package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.Controller;
import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.service.RoleService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashSet;

/**
 * Controller Class to handle a collection of {@link Role}s.
 *
 * @author Robin
 * @author Marius
 */
public class RolesController extends Controller {

    /**
     * Injected instance of RoleService
     */
    @Inject
    private RoleService roleService;

    /**
     * Hashset of entities
     */
    private HashSet<Role> entities = new HashSet<Role>();

    /**
     * init method of RolesController
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * refresh method of RolesController
     */
    public void refresh() {
        this.entities = roleService.getAll();
    }

}
