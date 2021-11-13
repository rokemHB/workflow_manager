package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.CarrierTypeService;
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
 * Resource class to handle the CarrierType Entity.
 *
 * @author Robin
 * @author Marius
 */
@Path("/carrier/type")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class CarrierTypeResource implements CRUDResource<CarrierType> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(CarrierTypeResource.class);

    /**
     * CarrierType Service to handle carrierType related CRUD operations
     */
    @Inject
    private CarrierTypeService carrierTypeService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new carrierType from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created carrierType or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(CarrierType entity) {
        try {
            return this.json.marshal(carrierTypeService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurrerd during carrierType createion:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new carrierType from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated carrierType or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(CarrierType entity) {
        try {
            return this.json.marshal(carrierTypeService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during carrierType update:", e);
            return makeResponseMessage("CarrierType update failed.");
        }
    }

    /**
     * Delete carrierType
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
            CarrierType carrierType = carrierTypeService.getById(parsedBody.get("id").getAsString());
            carrierTypeService.delete(carrierType);
            return makeResponseMessage("Carrier Type deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during carrierType deletion {}", e.getMessage());
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Get all carrierTypes existing in the system.
     *
     * @return json representation of all existing carrierTypes.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        try {
            List<CarrierType> carrierTypes = carrierTypeService.getAll();
            return this.json.marshal(carrierTypes);
        } catch (FindByException e) {
            return makeResponseMessage("#dankeArvid, die Tests darfst du selber fixen!");
        }
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
            CarrierType carrierType = carrierTypeService.getById(id);
            return carrierType != null ? json.marshal(carrierType) : makeResponseMessage("CarrierType not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during carrierType fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. CarrierType couldn't be fetched.");
        }
    }
}
