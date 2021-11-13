package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.ProcedureService;
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
 * Resource class to handle the Procedure Entity.
 *
 * @author Robin
 */
@Path("/procedure")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class ProcedureResource implements CRUDResource<Procedure> {

    /**
     * Logger object of the ProcedureResource class
     */
    private static final Logger logger = LogManager.getLogger(ProcedureResource.class);

    /**
     * Procedure Service to handle procedure related CRUD operations
     */
    @Inject
    private ProcedureService procedureService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new Procedure from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created procedure or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Procedure entity) {
        try {
            return this.json.marshal(procedureService.create(entity));
        } catch (CreationException e) {
            logger.debug("Error occurred during procedure creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new procedure from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated procedure or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Procedure entity) {
        try {
            return this.json.marshal(procedureService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during procedure update:", e);
            return makeResponseMessage("Procedure update failed.");
        }
    }

    /**
     * Delete procedure
     *
     * @param requestBody containing json with an unique identifier of the procedure to be deleted.
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
            Procedure procedure = procedureService.getById(parsedBody.get("id").getAsString());
            procedureService.delete(procedure);
            return makeResponseMessage("Procedure deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during procedure deletion {}", e.getMessage());
            return makeResponseMessage("Procedure deletion failed.");
        }
    }

    /**
     * Get all procedures existing in the system.
     *
     * @return json representation of all existing procedures.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Procedure> procedures = procedureService.getAll();
        return this.json.marshal(procedures);
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
            Procedure procedure = procedureService.getById(id);
            return procedure != null ? json.marshal(procedure) : makeResponseMessage("Procedure not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during procedure fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Procedure couldn't be fetched.");
        }
    }

}
