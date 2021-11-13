package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.ProcessStepService;
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
 * Class ProcessStepResource
 *
 * @author Robin
 * @author Marius
 */
@Path("/processstep")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class ProcessStepResource implements CRUDResource<ProcessStep> {

    /**
     * Logger object of the ProcessStepResource class
     */
    private static final Logger logger = LogManager.getLogger(ProcessStepResource.class);

    /**
     * ProcessStep Service to handle processStep related CRUD operations
     */
    @Inject
    private ProcessStepService processStepService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new ProcessStep from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created processStep or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(ProcessStep entity) {
        try {
            return this.json.marshal(processStepService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during process step creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new processStep from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated processStep or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(ProcessStep entity) {
        try {
            return this.json.marshal(processStepService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during process step update:", e);
            return makeResponseMessage("ProcessStep update failed.");
        }
    }

    /**
     * Delete processStep
     *
     * @param requestBody containing json with an unique identifier of the user to be deleted.
     * @return json response containing the status message
     */
    @DELETE
    @Override
    @RequiresAuthentication
    public String delete(String requestBody) {
        JsonObject parsedBody = new Gson().fromJson(requestBody, JsonObject.class);
        if (!parsedBody.has("id"))
            return makeResponseMessage("Invalid id.");
        try {
            ProcessStep processStep = processStepService.getById(parsedBody.get("id").getAsString());
            processStepService.delete(processStep);
            return makeResponseMessage("ProcessStep deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during process step deletion {}", e.getMessage());
            return makeResponseMessage("ProcessStep deletion failed.");
        }
    }

    /**
     * Get all processSteps existing in the system.
     *
     * @return json representation of all existing processSteps.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<ProcessStep> processSteps = processStepService.getAll();
        return this.json.marshal(processSteps);
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
    public String getById(String id) {
        try {
            ProcessStep processStep = processStepService.getById(id);
            return processStep != null ? json.marshal(processStep) : makeResponseMessage("ProcessStep not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during ProcessStep fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. ProcessStep couldn't be fetched.");
        }
    }
}
