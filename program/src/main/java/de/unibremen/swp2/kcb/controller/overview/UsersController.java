package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a collection of {@link User}s.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@ViewScoped
@Named("usersController")
public class UsersController extends OverviewController<User> {

    /**
     * Logger object of the UsersController class
     */
    private static final Logger logger = LogManager.getLogger(UsersController.class);

    /**
     * Injected instance of {@link UserService} to handle business logic
     */
    @Inject
    private UserService userService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of all {@link User}s.
     */
    @Override
    public void refresh() {
        List<User> users = userService.getAll();
        if (users == null || users.isEmpty()) {
            logger.debug("Users couldn't be loaded. UsersController is empty.");
            this.entities = new ArrayList<>();
            super.displayMessageFromResource("error.summary.empty-users",
                    "error.detail.empty-users", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.entities = users;
    }

    /**
     * Get User by ID.
     *
     * @param id the ID
     * @return User with that ID
     */
    @Override
    public User getById(String id) {
        try {
            return userService.getById(id);
        } catch (InvalidIdException e) {
            final String summary = localeController.formatString("error.summary.users-idValidation");
            final String detail = localeController.formatString("error.detail.users-idValidation");
            final String updatedDetail = detail != null ? detail.replace("<idString>", id) : null;
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }

    /**
     * Gets user by user name.
     *
     * @param name the name
     * @return the user by user name
     */
    public User getByUserName(String name) {
        if (name == null) {
            super.displayMessageFromResource("error.summary.username-null",
                    "error.detail.username-null", FacesMessage.SEVERITY_ERROR);
        }
        User user = userService.getByUsername(name);
        this.entities = new ArrayList<>();
        this.entities.add(user);
        return user;
    }

    /**
     * Gets user by email.
     *
     * @param email the email
     * @return the user by user name
     */
    public User getByEmail(String email) {
        if (email == null) {
            super.displayMessageFromResource("error.summary.email-null",
                    "error.detail.email-null", FacesMessage.SEVERITY_ERROR);
        }
        User user = userService.getByEmail(email);
        this.entities = new ArrayList<>();
        this.entities.add(user);
        return user;
    }

    /**
     * Gets user by first name.
     *
     * @param name the name
     * @return the user by first name
     */
    public List<User> getByFirstName(String name) {
        if (name == null) {
            super.displayMessageFromResource("error.summary.firstname-null",
                    "error.detail.firstname-null", FacesMessage.SEVERITY_ERROR);
        }
        this.entities = userService.getByFirstName(name);
        return this.entities;
    }

    /**
     * Gets user by last name.
     *
     * @param name the name
     * @return the user by last name
     */
    public List<User> getByLastName(String name) {
        if (name == null) {
            super.displayMessageFromResource("error.summary.lastname-null",
                    "error.detail.lastname-null", FacesMessage.SEVERITY_ERROR);
        }
        this.entities = userService.getByLastName(name);
        return this.entities;
    }

    /**
     * Convert designated roles to visual badge
     *
     * @param role to be converted
     * @return textstring to represent chosen badge
     */
    public String roleToBadge(Role role) {
        switch (role) {
            case ADMIN:
                return "bg-inverse-danger";
            case PKP:
                return "bg-inverse-success";
            case TRANSPORT:
                return "bg-inverse-warning";
            case LOGISTIKER:
                return "bg-inverse-primary";
            case TECHNOLOGE:
                return "bg-inverse-info";
            default:
                return "";
        }
    }

    /**
     * Return list of all Users with the Role 'Technologe' registered in the workflow manager.
     *
     * @return List of all Users with the Role 'Technologe'
     */
    public List<User> getAllTechnologen() {
        return userService.getByRole(Role.TECHNOLOGE);
    }
}