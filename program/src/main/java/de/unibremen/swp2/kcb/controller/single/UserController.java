package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.TransportService;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.EntityAlreadyExistingException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Controller class to handle {@link User}.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@ViewScoped
@Named("userController")
public class UserController extends SingleController<User> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(UserController.class);

    /**
     * User Service to handle actions
     */
    @Inject
    private UserService userService;

    /**
     * Transport Service to handle actions
     */
    @Inject
    private TransportService transportService;

    /**
     * List of roles
     */
    @Getter
    private ArrayList<String> roles = new ArrayList<>();

    /**
     * Array of all roles
     */
    @Getter
    private Role[] allRoles;

    /**
     * Instantiates a new User controller.
     */
    public UserController() {
        this.entity = new User();
    }

    /**
     * Refresh method of userController.
     */
    @PostConstruct
    public void refresh() {
        this.allRoles = Role.values();
    }

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            userService.create(this.entity);
            transportService.create(this.entity);
            userService.sendNewAccountMail(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during user creation.", e);
            final String username = this.getSaveUsername(entity);
            final String summary = localeController.formatString("error.summary.user-creation");
            final String detail = localeController.formatString("error.detail.user-creation");
            final String updatedDetail = detail != null ? detail.replace("<username>", username) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (MessagingException e) {
            logger.debug("Error while sending new Account E-Mail notification!");
            final String username = this.getSaveUsername(entity);
            final String summary = localeController.formatString("error.summary.user-creation-mail");
            final String detail = localeController.formatString("error.detail.user-creation-mail");
            final String updatedDetail = detail != null ? detail.replace("<username>", username) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("Error occurred during Workstation creation.", e);
            final String userName = this.getSaveUsername(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", userName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            transportService.delete(this.entity);
            userService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during user deletion.", e);
            final String username = this.getSaveUsername(entity);
            final String summary = localeController.formatString("error.summary.user-deletion");
            final String detail = localeController.formatString("error.detail.user-deletion");
            final String updatedDetail = detail != null ? detail.replace("<username>", username) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            userService.update(this.entity);
            transportService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during user update.", e);
            final String username = this.getSaveUsername(this.entity);
            final String summary = localeController.formatString("error.summary.user-update");
            final String detail = localeController.formatString("error.detail.user-update");
            final String updatedDetail = detail != null ? detail.replace("<username>", username) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }

    }

    /**
     * Get all roles.
     *
     * @return the roles
     */
    public Role[] getAllRoles() {
        return this.allRoles;
    }

    /**
     * Reset method of userController.
     */
    public void reset() {
        this.entity = new User();
    }

    /**
     * Get the name of the given entity and check for null references while doing so.
     *
     * @param entity to get username of
     * @return username if entity has one, 'null' otherwise
     */
    private String getSaveUsername(final User entity) {
        if (entity == null) return "null";
        if (entity.getUsername() == null) return "null";
        return entity.getUsername();
    }

    /**
     * Updates entity.
     *
     * @param entity the entity
     */
    public void updateEntity(User entity) {
        this.entity = entity;
        super.render("user_edit_form");
    }

    /**
     * Gets executing user.
     *
     * @return the executing user
     */
    public User getExecutingUser() {
        return userService.getExecutingUser();
    }

    /**
     * Print the current Users roles in a readable way
     *
     * @return a String containing all roles separated by comma
     */
    public String prettyPrintRoles() {
        Set<Role> roles = userService.getExecutingUser().getRoles();
        String result = "";

        if (roles.contains(Role.ADMIN))
            result += "Administrator, ";
        if (roles.contains(Role.TECHNOLOGE))
            result += "Technologe, ";
        if (roles.contains(Role.PKP))
            result += "Prozesskettenplaner, ";
        if (roles.contains(Role.LOGISTIKER))
            result += "Logistiker, ";
        if (roles.contains(Role.TRANSPORT))
            result += "Transporteur";

        if (result.endsWith(", "))
            result = result.substring(0, result.length() - 2);

        return result;
    }
}