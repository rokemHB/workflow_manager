package de.unibremen.swp2.kcb.controller;

import org.apache.shiro.SecurityUtils;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;

/**
 * Controller class to handle the even of an logout.
 *
 * @author Marius
 * @author Arvid
 */
@Named
@RequestScoped
public class LogoutController {

    /**
     * Shiro logout for the current user.
     */
    public void submit() throws IOException {
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        SecurityUtils.getSubject().logout();
        externalContext.invalidateSession();  // cleanup user related session state
        externalContext.redirect("login.xhtml?faces-redirect=true");
    }
}