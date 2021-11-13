package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.StateService;
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
 * Resource class to handle the State Entity.
 *
 * @author Robin
 */
@Path("/state")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class StateResource implements CRUDResource<State> {

    /**
     * Logger object of the StateResource class
     */
    private static final Logger logger = LogManager.getLogger(StateResource.class);

    /**
     * State Service to handle state related CRUD operations
     */
    @Inject
    private StateService stateService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new State from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created state or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(State entity) {
        try {
            return this.json.marshal(stateService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during state creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new state from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated state or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(State entity) {
        try {
            return this.json.marshal(stateService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during state update:", e);
            return makeResponseMessage("State update failed.");
        }
    }

    /**
     * Delete state
     *
     * @param requestBody containing json with an unique identifier of the state to be deleted.
     * @return json response containing the status message
     */
    @DELETE
    @Override
    @RequiresAuthentication
    public String delete(String requestBody) {
        JsonObject parsedBody = new Gson().fromJson(requestBody, JsonObject.class);
        if (!parsedBody.has("id"))
            return makeResponseMessage("Invalid id...");
        try {
            State state = stateService.getById(parsedBody.get("id").getAsString());
            stateService.delete(state);
            return makeResponseMessage("State deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during state deletion {}", e.getMessage());
            return makeResponseMessage("State deletion failed.");
        }
    }

    /**
     * Get all states existing in the system.
     *
     * @return json representation of all existing states.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<State> states = stateService.getAll();
        return this.json.marshal(states);
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
            State state = stateService.getById(id);
            return state != null ? json.marshal(state) : makeResponseMessage("State not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during state fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. State couldn't be fetched.");
        }
    }

}
