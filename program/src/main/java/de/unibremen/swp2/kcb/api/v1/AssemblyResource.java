package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.AssemblyService;
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
 * Resource class to handle the Assembly Entity.
 *
 * @author Robin
 * @author Marius
 */
@Path("/sample")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class AssemblyResource implements CRUDResource<Assembly> {

    /**
     * Logger object of the AssemblyResource class
     */
    private static final Logger logger = LogManager.getLogger(AssemblyResource.class);

    /**
     * Assembly Service to handle assembly related CRUD operations
     */
    @Inject
    private AssemblyService assemblyService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new Assembly from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created assembly or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Assembly entity) {
        try {
            return this.json.marshal(assemblyService.create(entity));
        } catch (CreationException e) {
            logger.debug("Error occurred during assembly creation: ", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new assembly from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated assembly or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Assembly entity) {
        try {
            return this.json.marshal(assemblyService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during assembly update:", e);
            return makeResponseMessage("Assembly update failed.");
        }
    }

    /**
     * Delete assembly
     *
     * @param requestBody containing json with an unique identifier of the assembly to be deleted.
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
            Assembly assembly = assemblyService.getById(parsedBody.get("id").getAsString());
            assemblyService.delete(assembly);
            return makeResponseMessage("Assembly deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during assembly deletion {}", e.getMessage());
            return makeResponseMessage("Assembly deletion failed.");
        }
    }

    /**
     * Get all assemblies existing in the system.
     *
     * @return json representation of all existing assemblies.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Assembly> assemblies = assemblyService.getAll();
        return this.json.marshal(assemblies);
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
            Assembly assembly = assemblyService.getById(id);
            return assembly != null ? json.marshal(assembly) : makeResponseMessage("Assembly not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during assembly fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Assembly couldn't be fetched.");
        }
    }
}
