package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.ProcessChainService;
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
 * Class ProcessChainResource
 *
 * @author Robin
 * @author Marius
 */
@Path("/processchain")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class ProcessChainResource implements CRUDResource<ProcessChain> {

    /**
     * Logger object of the ProcessChainResource class
     */
    private static final Logger logger = LogManager.getLogger(ProcessChainResource.class);

    /**
     * ProcessChain Service to handle processChain related CRUD operations
     */
    @Inject
    private ProcessChainService processChainService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new processChain from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created processChain or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(ProcessChain entity) {
        try {
            return this.json.marshal(processChainService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during process chain creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new processChain from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated processChain or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(ProcessChain entity) {
        try {
            return this.json.marshal(processChainService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during process chain update:", e);
            return makeResponseMessage("ProcessChain update failed.");
        }
    }

    /**
     * Delete processChain
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
            ProcessChain processChain = processChainService.getById(parsedBody.get("id").getAsString());
            processChainService.delete(processChain);
            return makeResponseMessage("ProcessChain deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during process chain deletion {}", e.getMessage());
            return makeResponseMessage("ProcessChain deletion failed.");
        }
    }

    /**
     * Get all processChain existing in the system.
     *
     * @return json representation of all existing processChains.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<ProcessChain> processChains = processChainService.getAll();
        return this.json.marshal(processChains);
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
            ProcessChain processChain = processChainService.getById(id);
            return processChain != null ? json.marshal(processChain) : makeResponseMessage("ProcessChain not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during ProcessChain fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. ProcessChain couldn't be fetched.");
        }
    }
}
