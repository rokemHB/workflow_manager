package de.unibremen.swp2.kcb.tags;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

/**
 * Class of a UIComponent that renders if the executing user got the provided role.
 *
 * @author Marius
 * @author Arvid
 */
@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
        tagName = "authConditional", value = "de.unibremen.swp2.kcb.tags.AuthConditional")
public class AuthConditional extends UIComponentBase {

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.AuthConditional";

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
     * Check whether or not the component should be rendered. Will check if the executing Subject
     * is authenticated.
     *
     * @return does this component children?
     */
    @Override
    public boolean isRendered() {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.isAuthenticated();
    }
}
