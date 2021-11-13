package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.StockService;
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
 * Resource class to handle the Stock Entity.
 *
 * @author Robin
 */
@Path("/stock")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class StockResource implements CRUDResource<Stock> {


    /**
     * Logger object of the StockResource class
     */
    private static final Logger logger = LogManager.getLogger(StockResource.class);

    /**
     * Stock Service to handle stock related CRUD operations
     */
    @Inject
    private StockService stockService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new Stock from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created stock or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Stock entity) {
        try {
            return this.json.marshal(stockService.create(entity));
        } catch (CreationException e) {
            logger.debug("Error occurred during stock creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new stock from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated stock or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Stock entity) {
        try {
            return this.json.marshal(stockService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during stock update:", e);
            return makeResponseMessage("Stock update failed.");
        }
    }

    /**
     * Delete stock
     *
     * @param requestBody containing json with an unique identifier of the stock to be deleted.
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
            Stock stock = stockService.getById(parsedBody.get("id").getAsString());
            stockService.delete(stock);
            return makeResponseMessage("Stock deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during stock deletion {}", e.getMessage());
            return makeResponseMessage("Stock deletion failed.");
        }
    }

    /**
     * Get all stocks existing in the system.
     *
     * @return json representation of all existing stocks.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Stock> stocks = stockService.getAll();
        return this.json.marshal(stocks);
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
            Stock stock = stockService.getById(id);
            return stock != null ? json.marshal(stock) : makeResponseMessage("Stock not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during stock fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Stock couldn't be fetched.");
        }
    }
}
