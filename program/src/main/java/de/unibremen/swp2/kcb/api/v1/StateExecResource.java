package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.StateExecService;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
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
 * Resource class to handle the StateExec Entity.
 *
 * @author Robin
 */
@Path("/stateexec")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class StateExecResource implements CRUDResource<StateExec> {

    /**
     * Logger object of the StateExec
     *Resource class
     */
    private static final Logger logger = LogManager.getLogger(StateExecResource.class);

    /**
     * StateExec
     *Service to handle stateExec related CRUD operations
     */
    @Inject
    private StateExecService stateExecService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new StateExec
     * from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created stateExec or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(StateExec entity) {
        return makeResponseMessage("StateExecs can not be created");

    }

    /**
     * Update a new stateExec from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated stateExec or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(StateExec entity) {
        try {
            return this.json.marshal(stateExecService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during stateExec update:", e);
            return makeResponseMessage("StateExec update failed.");
        }
    }

    /**
     * Delete stateExec
     *
     * @param requestBody containing json with an unique identifier of the stateExec to be deleted.
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
            StateExec stateExec = stateExecService.getById(parsedBody.get("id").getAsString());
            stateExecService.delete(stateExec);
            return makeResponseMessage("StateExec"
              +  "deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during stateExec deletion {}", e.getMessage());
            return makeResponseMessage("StateExec deletion failed.");
        }
    }

    /**
     * Get all stateExecs existing in the system.
     *
     * @return json representation of all existing stateExecs.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<StateExec> stateExecs = stateExecService.getAll();
        return this.json.marshal(stateExecs);
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
            StateExec stateExec = stateExecService.getById(id);
            return stateExec != null ? json.marshal(stateExec) : makeResponseMessage("StateExec"
              +  "not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during stateExec fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. StateExec couldn't be fetched.");
        }
    }

}
