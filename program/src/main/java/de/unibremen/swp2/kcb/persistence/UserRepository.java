package de.unibremen.swp2.kcb.persistence;


import de.unibremen.swp2.kcb.model.User;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for User.
 *
 * @see User
 *
 * @author Marc
 * @author Marius
 */
@Repository(forEntity = User.class)
public interface UserRepository extends EntityRepository<User, String> {

    /**
     * Find all Users with a certain firstname
     *
     * @param name the firstname to be searched
     * @return all Users with that firstname
     */
    List<User> findByFirstName(String name);

    /**
     * Find all Users with a certain lastname
     *
     * @param name the lastname to be searched
     * @return all Users with that lastname
     */
    List<User> findByLastName(String name);

    /**
     * Find all Users with a certain E-Mail-Address
     *
     * @param mail the E-Mail to be searched
     * @return all Users with that E-Mail
     */
    List<User> findByEmail(String mail);

    /**
     * Find all Users with a certain username
     *
     * @param name the username to be searched
     * @return all Users with that username - should be only one since username is unique
     */
    List<User> findByUsername(String name);
}
