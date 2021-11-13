package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.GlobalConfig;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.GlobalConfigService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
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
 * Resource class to handle the GlobalConfig Entity.
 *
 * @author Robin
 */
@Path("/globalconfig")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class GlobalConfigResource implements CRUDResource<GlobalConfig> {

    /**
     * Logger object of the GlobalConfigResource class
     */
    private static final Logger logger = LogManager.getLogger(GlobalConfigResource.class);

    /**
     * GlobalConfig Service to handle globalConfig related CRUD operations
     */
    @Inject
    private GlobalConfigService globalConfigService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new GlobalConfig from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created globalConfig or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(GlobalConfig entity) {
        try {
            return this.json.marshal(globalConfigService.create(entity));
        } catch (CreationException e) {
            logger.debug("Error occurred during globalConfig creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new globalConfig from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated globalConfig or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(GlobalConfig entity) {
        try {
            return this.json.marshal(globalConfigService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during globalConfig update:", e);
            return makeResponseMessage("GlobalConfig update failed.");
        }
    }

    /**
     * Delete globalConfig
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
            return makeResponseMessage("Invalid id...");
        try {
            GlobalConfig globalConfig = globalConfigService.getByKey(parsedBody.get("id").getAsString());
            globalConfigService.delete(globalConfig);
            return makeResponseMessage("GlobalConfig deleted successfully.");
        } catch (DeletionException | FindByException e) {
            logger.debug("Error occurred during globalConfig deletion {}", e.getMessage());
            return makeResponseMessage("GlobalConfig deletion failed.");
        }
    }

    /**
     * Get all globalConfigs existing in the system.
     *
     * @return json representation of all existing globalConfigs.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<GlobalConfig> globalConfig = globalConfigService.getAll();
        return this.json.marshal(globalConfig);
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
            GlobalConfig globalConfig = globalConfigService.getByKey(id);
            return globalConfig != null ? json.marshal(globalConfig) : makeResponseMessage("GlobalConfig not found.");
        } catch (FindByException e) {
            logger.debug("Error occurred during GlobalConfig fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. GlobalConfig couldn't be fetched.");
        }
    }
}
