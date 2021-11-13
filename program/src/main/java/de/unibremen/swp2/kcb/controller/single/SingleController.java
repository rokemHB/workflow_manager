package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.controller.Controller;
import lombok.Getter;

/**
 * Controller abstract class to handle selection of single objects.
 *
 * @param <T> Type of the single object.
 *
 * @author Marius
 * @author Arvid
 * @author Sören
 */
public abstract class SingleController<T> extends Controller {

    /**
     * The entity.
     */
    @Getter
    protected T entity;

    /**
     * Creates a new entity.
     */
    public abstract void create();

    /**
     * Deletes an existing entity.
     */
    public abstract void delete();

    /**
     * Updates an existing entity with the information of a new entity.
     */
    public abstract void update();

}