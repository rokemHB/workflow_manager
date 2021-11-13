package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Resource class to handle the User Entity.
 *
 * @author Robin
 * @author Marius
 */
@Path("/user")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class UserResource implements CRUDResource<User> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(UserResource.class);

    /**
     * User Service to handle user related CRUD operations
     */
    @Inject
    private UserService userService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Get all users existing in the system.
     *
     * @return json representation of all existing users.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<User> users = userService.getAll();
        return this.json.marshal(users);
    }

    /**
     * Create a new user from request body.
     *
     * @param user parsed from request body json
     * @return json representation of created user or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(User user) {
        try {
            return this.json.marshal(userService.create(user));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during user creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new user from request body.
     *
     * @param user parsed from request body json
     * @return json representation of updated user or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(User user) {
        try {
            User stored = userService.getById(user.getId());
            // Password can't be changed by REST-API. Use password reset function for that purpose
            user.setPassword(stored.getPassword());
            return this.json.marshal(userService.update(user));
        } catch (UpdateException e) {
            logger.debug("Error occurred during user update:", e);
            return makeResponseMessage(e.getMessage());
        } catch (InvalidIdException e) {
            logger.debug("error occurred during user update. User not updated.");
            return makeResponseMessage("User not updated. Invalid id.");
        }
    }

    /**
     * Delete user
     *
     * @param x containing json with an unique identifier of the user to be deleted.
     * @return json response containing the status message
     */
    @DELETE
    @Override
    @RequiresAuthentication
    public String delete(String x) {
        JsonObject requestBody = new Gson().fromJson(x, JsonObject.class);
        User user = this.getFromRequestBody(requestBody);
        if (user != null) {
            try {
                userService.delete(user);
                return makeResponseMessage("User " + user.toString() + " deleted.");
            } catch (DeletionException e) {
                logger.debug("Error occurred during user deletion: {}", e.getMessage());
                return makeResponseMessage("User deletion failed.");
            }
        }
        return makeResponseMessage("User not found.");
    }

    /**
     * Get Entity with the given id
     *
     * @return json response containing json representation of the entity
     */
    @Override
    @GET
    @RequiresAuthentication
    @Path("{id}")
    public String getById(@PathParam("id") String id) {
        try {
            User user = userService.getById(id);
            return user != null ? json.marshal(user) : makeResponseMessage("User not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during user fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. User not fetched.");
        }
    }

    /**
     * Gets the user object by an identifier specified in the request body. If multiple identifiers are given the first
     * match will be used. The order will be id -> username -> email
     *
     * @param requestBody to get user data from
     * @return User if user was found for given identifier or null if no user is found in the system
     */
    private User getFromRequestBody(final JsonObject requestBody) {
        try {
            if (requestBody.has("id"))
                return userService.getById(requestBody.get("id").getAsString());
            if (requestBody.has("username"))
                return userService.getByUsername(requestBody.get("username").getAsString());
            if (requestBody.has("email"))
                return userService.getByEmail(requestBody.get("email").getAsString());
        } catch (InvalidIdException e) {
            logger.debug("Invalid id exception occurred during rest call {}", e.getMessage());
            return null;
        }
        return null;
    }
}
