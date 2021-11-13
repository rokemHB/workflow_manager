package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.service.PriorityService;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a collection of {@link Priority}s.
 *
 * @author Arvid
 */
@ViewScoped
@Named("prioritiesController")
public class PrioritiesController extends OverviewController<Priority> {

    /**
     * Logger object of the PrioritysController class
     */
    private static final Logger logger = LogManager.getLogger(PrioritiesController.class);

    /**
     * Injected instance of {@link PriorityService} to handle business logic
     */
    @Inject
    private PriorityService priorityService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of all {@link Priority}s.
     */
    @Override
    public void refresh() {
        List<Priority> prioritys = priorityService.getAll();
        this.entities = new ArrayList<>();

        if (prioritys == null || prioritys.isEmpty()) {
            logger.debug("Prioritys couldn't be loaded. PrioritysController is empty.");
            super.displayMessageFromResource("error.summary.empty-prioritys",
                    "error.detail.empty-prioritys", FacesMessage.SEVERITY_ERROR);
        } else {
            this.entities = prioritys;
        }
    }

    /**
     * Get Priority by ID
     *
     * @param id the ID
     * @return Priority with that ID
     */
    @Override
    public Priority getById(String id) {
        Priority result = null;

        try {
            result = priorityService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.priority-by-id-not-found",
                    "error.detail.priority-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        }
        return result;
    }

    /**
     * Return the collection of all existing {@link Priority}s.
     *
     * @return All existing {@link Priority}s.
     */
    public List<Priority> getAll() {
        this.entities = new ArrayList<>();
        logger.debug("Querying all stored Prioritys.");

        this.entities = priorityService.getAll();
        return this.entities;
    }

    public String getColor(Priority priority) {
        return priorityService.getColor(priority);
    }
}
