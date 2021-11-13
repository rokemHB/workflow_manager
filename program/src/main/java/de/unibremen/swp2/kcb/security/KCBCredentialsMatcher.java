package de.unibremen.swp2.kcb.security;

import com.lambdaworks.crypto.SCryptUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * Credentials Matcher to handle scrypt credentials
 *
 * @author Marius
 */
public class KCBCredentialsMatcher implements CredentialsMatcher {

    /**
     * Logger object of the KCBCredentialsMatcher class
     */
    private static final Logger logger = LogManager.getLogger(KCBCredentialsMatcher.class);

    /**
     * Returns true if the provided token credentials match the stored account credentials, false otherwise.
     * Comparison of the subjects Principals (e.g. Email) will be case sensitive.
     *
     * @param authenticationToken the AuthenticationToken submitted during the authentication attempt
     * @param authenticationInfo  the AuthenticationInfo stored in the system.
     * @return true if the provided token credentials match the stored account credentials, false otherwise.
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        logger.trace("Apache Shiro - do credentials match");

        // Principal means User identifier - e.g. the email address of a user
        final String providedPrincipals = String.valueOf(authenticationToken.getPrincipal());
        final SimplePrincipalCollection storedPrincipals = (SimplePrincipalCollection) authenticationInfo.getPrincipals();

        // Check if provided principal is in storedPrincipals
        boolean found = false;
        for (Object p : storedPrincipals) {
            if (providedPrincipals.equals(String.valueOf(p))) {
                found = true;
                break;
            }
        }
        if (!found) return false;

        final String providedPassword = new String((char[]) authenticationToken.getCredentials());
        final String storedPassword = String.valueOf(authenticationInfo.getCredentials());

        return SCryptUtil.check(providedPassword, storedPassword);
    }
}
