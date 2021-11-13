package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.controller.Controller;
import lombok.Getter;

import java.util.List;

/**
 * Controller abstract class to handle a collection of objects.
 *
 * @param <T> Type of the collection of objects.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 * @author Sören
 */
public abstract class OverviewController<T> extends Controller {
    /**
     * The collection of entities.
     */
    @Getter
    protected List<T> entities;

    /**
     * Refresh the collection of all entities.
     */
    public abstract void refresh();


    /**
     * Gets Entity by id.
     *
     * @param id the id
     * @return the Entity by id
     */
    public abstract T getById(String id);
}