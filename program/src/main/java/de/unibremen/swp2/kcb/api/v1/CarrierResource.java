package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.CarrierService;
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
 * Resource class to handle the User Entity.
 *
 *
 * @author Robin
 * @author Marius
 */
@Path("/carrier")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class CarrierResource implements CRUDResource<Carrier> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(CarrierResource.class);

    /**
     * Carrier Service to handle carrier related CRUD operations
     */
    @Inject
    private CarrierService carrierService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new carrier from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created carrier or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Carrier entity) {
        try {
            return this.json.marshal(carrierService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during carrier creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new carrier from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated carrier or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Carrier entity) {
        try {
            return this.json.marshal(carrierService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during carrier update.", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Delete carrier
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
            return makeResponseMessage("Invalid request.");
        try {
            Carrier carrier = carrierService.getById(parsedBody.get("id").getAsString());
            carrierService.delete(carrier);
            return makeResponseMessage("Carrier deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during carrier deletion {}", e.getMessage());
            return makeResponseMessage("Carrier deletion failed.");
        }
    }

    /**
     * Get all carriers existing in the system.
     *
     * @return json representation of all existing carriers.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Carrier> carriers = carrierService.getAll();
        return this.json.marshal(carriers);
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
            Carrier carrier = carrierService.getById(id);
            return carrier != null ? json.marshal(carrier) : makeResponseMessage("Carrier not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during carrier fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Carrier couldn't be fetched.");
        }
    }
}
