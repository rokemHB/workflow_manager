package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.PriorityService;
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
 * Resource class to handle the Priority Entity.
 *
 * @author Robin
 */
@Path("/priority")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class PriorityResource implements CRUDResource<Priority> {

    /**
     * Logger object of the PriorityResource class
     */
    private static final Logger logger = LogManager.getLogger(PriorityResource.class);

    /**
     * Priority Service to handle priority related CRUD operations
     */
    @Inject
    private PriorityService priorityService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new Priority from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created priority or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Priority entity) {
        try {
            return this.json.marshal(priorityService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during priority creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new priority from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated priority or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Priority entity) {
        try {
            return this.json.marshal(priorityService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during priority update:", e);
            return makeResponseMessage("Priority update failed.");
        }
    }

    /**
     * Delete priority
     *
     * @param requestBody containing json with an unique identifier of the priority to be deleted.
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
            Priority priority = priorityService.getById(parsedBody.get("id").getAsString());
            priorityService.delete(priority);
            return makeResponseMessage("Priority deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during priority deletion {}", e.getMessage());
            return makeResponseMessage("Priority deletion failed.");
        }
    }

    /**
     * Get all priorities existing in the system.
     *
     * @return json representation of all existing priorities.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Priority> priorities = priorityService.getAll();
        return this.json.marshal(priorities);
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
            Priority priority = priorityService.getById(id);
            return priority != null ? json.marshal(priority) : makeResponseMessage("Priority not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during priority fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Priority couldn't be fetched.");
        }
    }

}
