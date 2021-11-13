package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.ValueService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
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
 * Resource class to handle the Value Entity.
 *
 * @author Robin
 */
@Path("/value")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class ValueResource implements CRUDResource<Value> {

    /**
     * Logger object of the ValueResource class
     */
    private static final Logger logger = LogManager.getLogger(ValueResource.class);

    /**
     * Value Service to handle value related CRUD operations
     */
    @Inject
    private ValueService valueService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new Value from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created value or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Value entity) {
        try {
            return this.json.marshal(valueService.create(entity));
        } catch (CreationException e) {
            logger.debug("Error occurred during value creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new value from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated value or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Value entity) {
        try {
            return this.json.marshal(valueService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during value update:", e);
            return makeResponseMessage("Value update failed.");
        }
    }

    /**
     * Delete value
     *
     * @param requestBody containing json with an unique identifier of the value to be deleted.
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
            Value value = valueService.getById(parsedBody.get("id").getAsString());
            valueService.delete(value);
            return makeResponseMessage("Value deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during value deletion {}", e.getMessage());
            return makeResponseMessage("Value deletion failed.");
        }
    }

    /**
     * Get all values existing in the system.
     *
     * @return json representation of all existing values.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Value> values = valueService.getAll();
        return this.json.marshal(values);
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
            Value value = valueService.getById(id);
            return value != null ? json.marshal(value) : makeResponseMessage("Value not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during value fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Value couldn't be fetched.");
        }
    }
}
