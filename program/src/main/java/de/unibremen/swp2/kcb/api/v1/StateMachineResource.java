package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.StateMachineService;
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
 * Resource class to handle the StateMachine Entity.
 *
 * @author Robin
 */
@Path("/statemachine")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class StateMachineResource implements CRUDResource<StateMachine> {

    /**
     * Logger object of the StateMachineResource class
     */
    private static final Logger logger = LogManager.getLogger(StateMachineResource.class);

    /**
     * StateMachine Service to handle stateHistory related CRUD operations
     */
    @Inject
    private StateMachineService stateHistoryService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new StateMachine from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created stateHistory or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(StateMachine entity) {
        try {
            return this.json.marshal(stateHistoryService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during stateHistory creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new stateHistory from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated stateHistory or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(StateMachine entity) {
        try {
            return this.json.marshal(stateHistoryService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during stateHistory update:", e);
            return makeResponseMessage("StateMachine update failed.");
        }
    }

    /**
     * Delete stateHistory
     *
     * @param requestBody containing json with an unique identifier of the stateHistory to be deleted.
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
            StateMachine stateHistory = stateHistoryService.getById(parsedBody.get("id").getAsString());
            stateHistoryService.delete(stateHistory);
            return makeResponseMessage("StateMachine deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during stateHistory deletion {}", e.getMessage());
            return makeResponseMessage("StateMachine deletion failed.");
        }
    }

    /**
     * Get all stateHistories existing in the system.
     *
     * @return json representation of all existing stateHistories.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<StateMachine> stateHistories = stateHistoryService.getAll();
        return this.json.marshal(stateHistories);
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
            StateMachine stateHistory = stateHistoryService.getById(id);
            return stateHistory != null ? json.marshal(stateHistory) : makeResponseMessage("StateMachine not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during stateHistory fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. StateMachine couldn't be fetched.");
        }
    }

}
