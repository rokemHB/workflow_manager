package de.unibremen.swp2.kcb.tags;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class of a UIComponent that renders if the executing user got the provided role.
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
        tagName = "roleConditional", value = "de.unibremen.swp2.kcb.tags.RoleConditional")
public class RoleConditional extends UIComponentBase {

    /**
     * Logger object of the RoleConditional class
     */
    private static final Logger logger = LogManager.getLogger(RoleConditional.class);

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.RoleConditional";

    /**
     * Return family of this UIComponent
     *
     * @return the family
     */
    @Override
    public String getFamily() {
        return FAMILY;
    }

    /**
     * Checks whether or not the component should be rendered. Will check if all required roles
     * are owned by the executing subject.
     * <p>
     * Will determine the link between roles by the attribute 'operation'. Options are 'OR' and 'AND'.
     * Default operation = 'AND'.
     *
     * @return does this component render children?
     */
    @Override
    public boolean isRendered() {
        logger.trace("Get rendered Children of RoleConditional.");
        Subject subject = SecurityUtils.getSubject();
        String roles = (String) getAttributes().get("roles");
        String opString = (String) getAttributes().get("operation");

        boolean or = opString != null && (opString.equals("or") || opString.equals("OR"));

        if (roles == null)
            throw new MissingAttributeException("Role attribute can't be null.");
        if (subject == null)
            return false;
        if (!subject.isAuthenticated())
            return false;
        List<String> result = parseRoleAttribute(roles);
        return or ? this.hasOneRole(subject, result) : subject.hasAllRoles(result);
    }

    /**
     * Comma separated string of roles required for this component to be rendered
     *
     * @param roles comma separated string of roles
     * @return the roles
     */
    private ArrayList<String> parseRoleAttribute(String roles) {
        String[] parts = roles.split(",");
        ArrayList<String> result = new ArrayList<>();
        for (String part : parts) {
            result.add(part.strip());
        }
        return result;
    }

    /**
     * Check if the given subject has one of the provided roles
     *
     * @param subject to check roles of
     * @param roles   to be checked
     * @return does the subject have one of the roles?
     */
    private boolean hasOneRole(Subject subject, Collection<String> roles) {
        for (String role : roles) {
            if (subject.hasRole(role))
                return true;
        }
        return false;
    }
}
