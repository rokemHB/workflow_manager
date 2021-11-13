package de.unibremen.swp2.kcb.security;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;

import java.util.List;

/**
 * Class to check {@link Workstation} access.
 *
 * @author Marius
 */
public class WorkstationAccessProvider {

    /**
     * Check if a user is permitted to use a workstation. Return true if user has permission, false otherwise.
     *
     * @param workstation to be used by the user
     * @param user        to check access of
     * @return true if user is allowed to workstation, false otherwise
     */
    public boolean checkAccess(final Workstation workstation, final User user) {
        if (user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.PKP))
            return true;

        List<User> allowedUsers = workstation.getUsers();
        return allowedUsers.contains(user);
    }
}
