package de.unibremen.swp2.kcb.api.v1;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.unibremen.swp2.kcb.model.KCBEntity;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Interface for all Resources made available via REST-API
 * and implement basic CRUD operations
 *
 * @param <T>
 *
 * @author Marius
 */
public interface CRUDResource<T extends KCBEntity> {

    /**
     * Create entity
     *
     * @param entity to be created
     * @return string representation of the entity or error message if creation failed.
     */
    String create(T entity);

    /**
     * Update an existing entity with the given unique identifier (id).
     *
     * @param entity to be updated. ID attribute must match an existing entity id
     * @return json representation of updated entity.
     */
    String update(T entity);

    /**
     * Delete entity
     *
     * @param requestBody containing json with an unique identifier of the entity to be deleted.
     * @return json response containing the status message
     */
    String delete(String requestBody);

    /**
     * Get all entities of this resource.
     *
     * @return json response with all entities of this resource
     */
    String getAll();

    /**
     * Get Entity with the given id
     *
     * @return json response containing json representation of the entity
     */
    @Path("{id}")
    String getById(@PathParam("id") String id);

    /**
     * Return a simple json string containing the given message
     *
     * @param message to be included in json
     * @return json string with given message
     */
    default String makeResponseMessage(final String message) {
        if (message == null) return "";
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("message", new JsonPrimitive(message));
        return jsonObject.toString();
    }
}
