package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.unibremen.swp2.kcb.model.ValidationPattern;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.ValidationPatternService;
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
 * Resource class to handle the ValidationPattern Entity.
 *
 * @author Robin
 */
@Path("/validationpattern")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class ValidationPatternResource implements CRUDResource<ValidationPattern> {

    /**
     * Logger object of the validationPatternResource class
     */
    private static final Logger logger = LogManager.getLogger(ValidationPatternResource.class);

    /**
     * ValidationPattern Service to handle validationPattern related CRUD operations
     */
    @Inject
    private ValidationPatternService validationPatternService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new validationPattern from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created validationPattern or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(ValidationPattern entity) {
        throw new UnsupportedOperationException("ValidationPatterns can not be created!");
    }

    /**
     * Update a new validationPattern from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated validationPattern or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(ValidationPattern entity) {
        try {
            return this.json.marshal(validationPatternService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during validationPattern update:", e);
            return makeResponseMessage("ValidationPattern update failed.");
        }
    }

    /**
     * Delete validationPattern
     *
     * @param requestBody containing json with an unique identifier of the validationPattern to be deleted.
     * @return json response containing the status message
     */
    @DELETE
    @Override
    @RequiresAuthentication
    public String delete(String requestBody) {
        throw new UnsupportedOperationException("ValidationPatterns can not be deleted!");
    }

    /**
     * Get all validationPatterns existing in the system.
     *
     * @return json representation of all existing validationPatterns.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<ValidationPattern> validationPatterns = validationPatternService.getAll();
        return this.json.marshal(validationPatterns);
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
        ValidationPattern validationPattern = validationPatternService.getByName(id);
        return validationPattern != null ? json.marshal(validationPattern) : makeResponseMessage("ValidationPattern not found.");
    }

    /**
     * Get Entity with the given id
     *
     * @return json response containing json representation of the entity
     */
    @GET
    @RequiresAuthentication
    @Path("/regex/{id}")
    public String getRegexById(@PathParam("id") String id) {
        String regEx = validationPatternService.getRegEx(id);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("key", new JsonPrimitive(id));
        jsonObject.add("value", new JsonPrimitive(regEx));
        return regEx != null ? json.marshal(jsonObject) : makeResponseMessage("ValidationPattern not found.");
    }

}
