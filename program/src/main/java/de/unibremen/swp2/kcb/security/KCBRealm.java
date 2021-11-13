package de.unibremen.swp2.kcb.security;

import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Realm to handle authentication and authorization in the Workflow Manager
 * <p>
 * Siehe auch CDI mit Shiro (https://issues.apache.org/jira/browse/SHIRO-337, https://issues.apache.org/jira/browse/SHIRO-422)
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public class KCBRealm extends JdbcRealm {

    /**
     * Logger object of the KCBRealm class
     */
    private static final Logger logger = LogManager.getLogger(KCBRealm.class);

    /**
     * EntityManager since Shiro doesn't support CDI
     */
    private EntityManager em;

    /**
     * Instantiates a new Kcb realm.
     */
    public KCBRealm() {
        super();
        this.setName("KCBRealm");
        // Use the custom scrypt credentials matcher
        KCBCredentialsMatcher credentialsMatcher = new KCBCredentialsMatcher();
        this.setCredentialsMatcher(credentialsMatcher);
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("prototypePU");
        this.em = emFactory.createEntityManager();
    }

    /**
     * Does the Realm support the given authenticationToken?
     * Will return true for every authenticationToken, since this is the only Realm used.
     *
     * @param authenticationToken to check
     * @return always true
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return true;
    }

    /**
     * Gets the authenticationInfo from authentication token
     * @param token
     * @return the AuthenticationInfo object
     * @throws AuthenticationException when authentication fails
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String enteredPrincipal = String.valueOf(token.getPrincipal());
        checkNull(enteredPrincipal, "Entered email is null.");
        String enteredPassword = new String((char[]) token.getCredentials()); //Token.getCredentials ist char[]
        checkNull(enteredPassword, "Entered password is null", false);

        User user = getUserByPrincipal(enteredPrincipal);

        checkNull(user, "No account found for user with principal " + enteredPrincipal);
        final String password = user.getPassword();
        PrincipalCollection pc = this.getPrincipalCollection(user);
        // Clear shiro caches and refresh the user - e.g. changed roles
        return new SimpleAuthenticationInfo(pc, password);
    }

    /**
     * Gets the authenticationInfo from PrincipalCollection
     * @param principals
     * @return the AuthenticationInfo object
     * @throws AuthenticationException when authentication fails
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        final String principal = String.valueOf(principals.getPrimaryPrincipal());
        checkNull(principal, "Principal was null.");

        User user = getUserByPrincipal(principal);

        Set<String> roles = new HashSet<>();

        if (user == null || user.getRoles() == null)
            return null;

        for (Role r : user.getRoles()) {
            roles.add(r.toString());
        }
        return new SimpleAuthorizationInfo(roles);
    }

    /**
     * Return the user with the given Principal
     *
     * @param enteredPrincipal to get user for
     * @return User for given principal or null if user is not found.
     */
    private User getUserByPrincipal(String enteredPrincipal) {
        List<User> users;
        if (isEmail(enteredPrincipal))
            users = (List<User>) this.em.createNamedQuery("findUsersByEmail")
                    .setParameter("email", enteredPrincipal).getResultList();
        else
            users = (List<User>) this.em.createNamedQuery("findUsersByUsername")
                    .setParameter("username", enteredPrincipal).getResultList();
        if (users == null || users.size() == 0)
            return null;

        if (users.size() > 1)
            logger.warn("Found multiple Users for principal {}. Principals should be unique.", enteredPrincipal);

        return users.get(0);
    }

    /**
     * Checks if a given obj is null. Will throw AuthenticationException if so.
     *
     * @param obj     to check
     * @param log     should entity be logged
     * @param message to be displayed when check fails.
     */
    private void checkNull(Object obj, String message, boolean log) {
        if (obj == null || obj.toString().equals("null")) {
            if (log)
                logger.debug(message);
            throw new AuthenticationException(message);
        }
        if (log)
            logger.debug("{} passed null check.", obj);
    }

    /**
     * Checks if a given obj is null. Will throw AuthenticationException if so.
     *
     * @param obj     to check
     * @param message to be displayed when check fails.
     */
    private void checkNull(Object obj, String message) {
        checkNull(obj, message, false);
    }

    /**
     * Checks whether or not a given string is an email address or not
     *
     * @param value to be checked
     * @return is given value an email address?
     */
    private boolean isEmail(String value) {
        return value.contains("@");
    }

    /**
     * Clear cached data for given principal and user. Use this on login to fetch "clean" user object from DB.
     *
     * @param user to be refreshed
     */
    public void refresh(final User user) {
        PrincipalCollection pc = this.getPrincipalCollection(user);
        User stored = this.getUserByPrincipal(user.getEmail());
        if (stored != null) em.refresh(stored);
        super.doClearCache(pc);
    }

    /**
     * Clear cached data for given principal and user. Use this on login to fetch "clean" user object from DB.
     *
     * @param principal of user to be updated
     */
    public void refresh(final String principal) {
        User stored = this.getUserByPrincipal(principal);
        if (stored != null) em.refresh(stored);
    }

    /**
     * Generate a principal collection for the given user.
     *
     * @param user to get principal collection for
     * @return PrincipalCollection with all valid Principals of the given user
     */
    private PrincipalCollection getPrincipalCollection(final User user) {
        SimplePrincipalCollection pc = new SimplePrincipalCollection();
        pc.add(user.getUsername(), this.getName());
        pc.add(user.getEmail(), this.getName());
        return pc;
    }
}
