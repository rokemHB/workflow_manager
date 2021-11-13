package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.service.ParameterService;
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
 * Class ParametersController
 *
 * @author Marc
 * @author Robin
 * @author Arvid
 */
@Named
@ViewScoped
public class ParametersController extends OverviewController<Parameter> {

    /**
     * Logger object of the JobsController class
     */
    private static final Logger logger = LogManager.getLogger(ParametersController.class);

    /**
     * Injected instance of {@link ParameterService} to handle business logic
     */
    @Inject
    private ParameterService parameterService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of of all {@link Job}s.
     */
    @Override
    public void refresh() {
        this.entities = new ArrayList<>();
        List<Parameter> parameters = parameterService.getAll();
        if (parameters == null || parameters.isEmpty()) {
            logger.debug("Parameters couldn't be loaded. ParametersController is empty.");
            super.displayMessageFromResource("error.summary.empty-parameters",
                    "error.detail.empty-parameters", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.entities = parameters;
    }

    /**
     * gets the parameter by its ID
     * @param id the id
     * @return the location
     */
    @Override
    public Parameter getById(String id) {
        try {
            return parameterService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.parameter-by-id-not-found",
                    "error.detail.parameter-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return null;
    }
}
