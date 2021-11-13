package de.unibremen.swp2.kcb.controller;

import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller class to set users theme.
 *
 * @author Marius
 */
@Named
@SessionScoped
public class ThemeController extends Controller {

    /**
     * Logger object of the ThemeController class
     */
    private static final Logger logger = LogManager.getLogger(ThemeController.class);

    /**
     * Instance of user service to access executing user
     */
    @Inject
    private UserService userService;

    /**
     * Method called after ThemeController is constructed
     */
    @PostConstruct
    public void setup() {
        User currentUser = userService.getExecutingUser();
        if (currentUser != null && currentUser.isDarkMode())
            this.setDarkModeCookie(currentUser);
    }

    /**
     * Update the dark mode cookie for HttpServletRequest
     *
     * @param currentUser to set dark mode cookie for
     */
    public void setDarkModeCookie(User currentUser) {
        if (currentUser == null) return;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) return;
        ExternalContext externalContext = facesContext.getExternalContext();
        if (externalContext == null) return;
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("darkmode")) {
                cookie.setValue(currentUser.isDarkMode() ? "true" : "false");
                HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
                response.addCookie(cookie);
                return;
            }
        }
        Cookie cookie = new Cookie("darkmode", currentUser.isDarkMode() ? "true" : "false");
        cookie.setMaxAge(-1);
        cookie.setPath(request.getContextPath());
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        response.addCookie(cookie);
    }

    /**
     * Enables the dark mode for the currently executing user
     */
    public void setDarkMode() {
        this.updateTheme(true);
        super.redirect("settings.xhtml");
    }

    /**
     * Enables the light mode for the currently executing user
     */
    public void setLightMode() {
       this.updateTheme(false);
       super.redirect("settings.xhtml");
    }

    /**
     * Update the users theme. Will set darkmode to the given argument
     *
     * @param darkMode should darkmode be enabled?
     */
    private void updateTheme(boolean darkMode) {
        User currentUser = userService.getExecutingUser();
        if (currentUser == null) return;
        currentUser.setDarkMode(darkMode);
        try {
            currentUser = userService.update(currentUser);
            this.setDarkModeCookie(currentUser);
        } catch (UpdateException e) {
            logger.debug(e);
        }
    }
}
