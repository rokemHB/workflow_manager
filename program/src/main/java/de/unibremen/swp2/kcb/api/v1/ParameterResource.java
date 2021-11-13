package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.ParameterService;
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
 * Resource class to handle the Parameter Entity.
 *
 * @author Robin
 */
@Path("/parameter")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class ParameterResource implements CRUDResource<Parameter> {

    /**
     * Logger object of the ParameterResource class
     */
    private static final Logger logger = LogManager.getLogger(ParameterResource.class);

    /**
     * Parameter Service to handle parameter related CRUD operations
     */
    @Inject
    private ParameterService parameterService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new Parameter from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created parameter or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Parameter entity) {
        try {
            return this.json.marshal(parameterService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during parameter creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new parameter from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated parameter or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Parameter entity) {
        try {
            return this.json.marshal(parameterService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during parameter update:", e);
            return makeResponseMessage("Parameter update failed.");
        }
    }

    /**
     * Delete parameter
     *
     * @param requestBody containing json with an unique identifier of the parameter to be deleted.
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
            Parameter parameter = parameterService.getById(parsedBody.get("id").getAsString());
            parameterService.delete(parameter);
            return makeResponseMessage("Parameter deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during parameter deletion {}", e.getMessage());
            return makeResponseMessage("Parameter deletion failed.");
        }
    }

    /**
     * Get all parameters existing in the system.
     *
     * @return json representation of all existing parameters.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Parameter> parameters = parameterService.getAll();
        return this.json.marshal(parameters);
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
            Parameter parameter = parameterService.getById(id);
            return parameter != null ? json.marshal(parameter) : makeResponseMessage("Parameter not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during parameter fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Parameter couldn't be fetched.");
        }
    }

}
