package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;

import java.util.HashSet;

/**
 * Service class to handle Roles
 *
 * @author Robin
 * @author Marius
 */
public class RoleService implements Service<Role> {

    /**
     * Create method of RoleService - roles can never be created
     * @param entity to be created and persisted
     * @return nothing
     * @throws CreationException never thrown
     */
    @Override
    public Role create(Role entity) throws CreationException {
        throw new UnsupportedOperationException("Not working.");
    }

    /**
     * Create method of RoleService - roles can never be created
     * @param entity to be created and persisted
     * @return nothing
     * @throws UpdateException never thrown
     */
    @Override
    public Role update(Role entity) throws UpdateException {
        throw new UnsupportedOperationException("Not working.");
    }

    /**
     * Create method of RoleService - roles can never be created
     * @param entity to be created and persisted
     * @throws DeletionException never thrown
     */
    @Override
    public void delete(Role entity) throws DeletionException {
        throw new UnsupportedOperationException("Not working.");
    }

    /**
     * Gets all Roles as HashSet
     * @return HashSet will all roles
     */
    public HashSet<Role> getAll() {
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        roles.add(Role.TECHNOLOGE);
        roles.add(Role.LOGISTIKER);
        roles.add(Role.PKP);
        roles.add(Role.TRANSPORT);
        return roles;
    }

}
