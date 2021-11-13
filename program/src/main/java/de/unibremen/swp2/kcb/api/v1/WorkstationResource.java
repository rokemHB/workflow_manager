package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.WorkstationService;
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
 * Resource class to handle the Workstation Entity.
 *
 * @author Robin
 * @author Marius
 */
@Path("/workstation")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class WorkstationResource implements CRUDResource<Workstation> {

    /**
     * Logger object of the WorkstationResource class
     */
    private static final Logger logger = LogManager.getLogger(WorkstationResource.class);

    /**
     * Workstation Service to handle workstation related CRUD operations
     */
    @Inject
    private WorkstationService workstationService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new Workstation from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created workstation or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Workstation entity) {
        try {
            return this.json.marshal(workstationService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during workstation creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new workstation from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated workstation or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Workstation entity) {
        try {
            return this.json.marshal(workstationService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during workstation update:", e);
            return makeResponseMessage("Workstation update failed.");
        }
    }

    /**
     * Delete workstation
     *
     * @param requestBody containing json with an unique identifier of the workstation to be deleted.
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
            Workstation workstation = workstationService.getById(parsedBody.get("id").getAsString());
            workstationService.delete(workstation);
            return makeResponseMessage("Workstation deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during workstation deletion {}", e.getMessage());
            return makeResponseMessage("Workstation deletion failed.");
        }
    }

    /**
     * Get all workstations existing in the system.
     *
     * @return json representation of all existing workstations.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Workstation> workstations = workstationService.getAll();
        return this.json.marshal(workstations);
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
            Workstation workstation = workstationService.getById(id);
            return workstation != null ? json.marshal(workstation) : makeResponseMessage("Workstation not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during workstation fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Workstation couldn't be fetched.");
        }
    }

}
