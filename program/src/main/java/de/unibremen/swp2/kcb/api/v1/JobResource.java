package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.JobService;
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
 * Resource class to handle the Job Entity.
 *
 * @author Robin
 */
@Path("/job")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@KCBSecure
public class JobResource implements CRUDResource<Job> {

    /**
     * Logger object of the JobResource class
     */
    private static final Logger logger = LogManager.getLogger(JobResource.class);

    /**
     * Job Service to handle job related CRUD operations
     */
    @Inject
    private JobService jobService;

    /**
     * JsonUtil to handle json parsing
     */
    @Inject
    private JsonUtil json;

    /**
     * Create a new Job from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of created job or error message if creation fails
     */
    @POST
    @Override
    @RequiresAuthentication
    public String create(Job entity) {
        try {
            return this.json.marshal(jobService.create(entity));
        } catch (CreationException | EntityAlreadyExistingException e) {
            logger.debug("Error occurred during job creation:", e);
            return makeResponseMessage(e.getMessage());
        }
    }

    /**
     * Update a new job from request body.
     *
     * @param entity parsed from request body json
     * @return json representation of updated job or error message if update fails
     */
    @PUT
    @Override
    @RequiresAuthentication
    public String update(Job entity) {
        try {
            return this.json.marshal(jobService.update(entity));
        } catch (UpdateException e) {
            logger.debug("Error occurred during job update:", e);
            return makeResponseMessage("Job update failed.");
        }
    }

    /**
     * Delete job
     *
     * @param requestBody containing json with an unique identifier of the job to be deleted.
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
            Job job = jobService.getById(parsedBody.get("id").getAsString());
            jobService.delete(job);
            return makeResponseMessage("Job deleted successfully.");
        } catch (InvalidIdException | DeletionException e) {
            logger.debug("Error occurred during job deletion {}", e.getMessage());
            return makeResponseMessage("Job deletion failed.");
        }
    }

    /**
     * Get all jobs existing in the system.
     *
     * @return json representation of all existing jobs.
     */
    @GET
    @Override
    @RequiresAuthentication
    public String getAll() {
        List<Job> jobs = jobService.getAll();
        return this.json.marshal(jobs);
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
            Job job = jobService.getById(id);
            return job != null ? json.marshal(job)
              : makeResponseMessage("job not found.");
        } catch (InvalidIdException e) {
            logger.debug("Error occurred during job fetching {}", e.getMessage());
            return makeResponseMessage("Invalid id. Job couldn't be fetched.");
        }
    }

}
