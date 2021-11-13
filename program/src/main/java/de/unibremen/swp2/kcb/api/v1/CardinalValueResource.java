package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.parameter.CardinalValue;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.CardinalValueService;
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
 * Resource class to handle the CardinalValue Entity.
 *
 * @author Robin
 */
@Path("/cardinalvalue")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class CardinalValueResource implements CRUDResource<CardinalValue> {

    /**
     * Logger object of the CardinalValueResource class
     */
    private static final Logger logger = LogManager.getLogger(CardinalValueResource.class);

    /**
     * CardinalValue Service to handle CardinalValue related CRUD operations
     */
    @Inject
    private CardinalValueService cardinalValueService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new CardinalValue from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created CardinalValue or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(CardinalValue entity) {
        try {
            return this.json.marshal(cardinalValueService.create(entity));
        } catch (CreationException e) {
            logger.debug("Error occurred during cardinalValue creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new cardinalValue from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated cardinalValue or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(CardinalValue entity) {
        try {
            return this.json.marshal(cardinalValueService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during cardinalValue update:", e);
            return makeResponseMessage("CardinalValue update failed.");
        }
    }

    /**
     * Delete cardinalValue
     *
     * @param requestBody containing json with an unique identifier of the cardinalValue to be deleted.
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
            CardinalValue cardinalValue = cardinalValueService.getById(parsedBody.get("id").getAsString());
            cardinalValueService.delete(cardinalValue);
            return makeResponseMessage("CardinalValue deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during cardinalValue deletion {}", e.getMessage());
            return makeResponseMessage("CardinalValue deletion failed.");
        }
    }

    /**
     * Get all cardinalValues existing in the system.
     *
     * @return json representation of all existing cardinalValues.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<CardinalValue> cardinalValues = cardinalValueService.getAll();
        return this.json.marshal(cardinalValues);
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
            CardinalValue cardinalValue = cardinalValueService.getById(id);
            return cardinalValue != null ? json.marshal(cardinalValue) : makeResponseMessage("CardinalValue not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during cardinalValue fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. CardinalValue couldn't be fetched.");
        }
    }


}
