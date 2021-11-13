package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.controller.Controller;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller Class to handle a single users menu toggle.
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@SessionScoped
@Named
public class NavBarController extends Controller {

    /**
     * Logger object of the NavBarController class
     */
    private static final Logger logger = LogManager.getLogger(AssemblyController.class);

    /**
     * Injected instance of userService
     */
    @Inject
    private UserService userService;

    /**
     * User that is logged in current session
     */
    private User user;

    /**
     * Load executing user from userService
     */
    @PostConstruct
    public void setUp() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            this.user = userService.getExecutingUser();
        } else {
            logger.warn("Executing user couldn't be fetched from service. Menu Toggle wont be working as intended.");
            this.user = null;
        }
    }

    /**
     * Get static Class
     *
     * @return String static class if user has pinned attribute set or empty string
     */
    public String getStatic() {
        return this.user != null && this.user.isPinned() ? "static" : "";
    }

    /**
     * Get classes for menu static
     *
     * @return Menu static class if user has pinned attribute or empty string
     */
    public String getMenuStatic() {
        return this.user != null && this.user.isPinned() ? "menu-static active" : "";
    }

    /**
     * Toggle Menu for currently executing user
     */
    public void toggleMenu() {
        this.user = userService.toggleMenu(user);
    }
}
