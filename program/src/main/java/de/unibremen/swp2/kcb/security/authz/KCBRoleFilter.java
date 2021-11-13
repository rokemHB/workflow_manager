package de.unibremen.swp2.kcb.security.authz;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Authorization Filter that checks if any of the given roles is owned by the given user.
 * The default implementation of apache shiro only checks for all required roles.
 *
 * @see RolesAuthorizationFilter
 *
 * @author Marius
 */
public class KCBRoleFilter extends RolesAuthorizationFilter {

    /**
     * {@inheritDoc}
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws IOException
     */
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            //no roles specified, so nothing to check - allow access.
            return true;
        }

        // check if subject has one of the given roles
        for (String role : rolesArray) {
            if (subject.hasRole(role))
                return true;
        }
        return false;
    }

}
