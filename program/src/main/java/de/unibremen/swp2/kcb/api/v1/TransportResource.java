package de.unibremen.swp2.kcb.api.v1;

import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.TransportService;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
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
 * Resource class to handle the Transport Entity.
 *
 * @author Robin
 * @author Arvid
 */
@Path("/sample")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class TransportResource implements CRUDResource<Transport> {

    /**
     * Logger object of the TransportResource class
     */
    private static final Logger logger = LogManager.getLogger(TransportResource.class);

    /**
     * Transport Service to handle transport related CRUD operations
     */
    @Inject
    private TransportService transportService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new Transport from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created transport or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Transport entity) {
        throw new UnsupportedOperationException("Transport can not be created!");
    }

    /**
     * Update a new transport from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated transport or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Transport entity) {
        throw new UnsupportedOperationException("Transport can not be updated!");
    }

    /**
     * Delete transport
     *
     * @param requestBody containing json with an unique identifier of the transport to be deleted.
     * @return json response containing the status message
     */
    @DELETE
    @Override
    @RequiresAuthentication
    public String delete(String requestBody) {
        throw new UnsupportedOperationException("Transport can not be deleted!");
    }

    /**
     * Get all transports existing in the system.
     *
     * @return json representation of all existing transports.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Transport> transports = transportService.getAll();
        return this.json.marshal(transports);
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
            Transport transport = transportService.getById(id);
            return transport != null ? json.marshal(transport) : makeResponseMessage("Transport not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during transport fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Transport couldn't be fetched.");
        }
    }

}
