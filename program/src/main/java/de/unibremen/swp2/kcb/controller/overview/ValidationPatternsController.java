package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.ValidationPattern;
import de.unibremen.swp2.kcb.service.ValidationPatternService;
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
 * Controller class to handle a collection of standard validation patterns
 *
 * @author Robin
 * @author Arvid
 */
@ViewScoped
@Named
public class ValidationPatternsController extends OverviewController<ValidationPattern> {

    /**
     * Logger object of the UsersController class
     */
    private static final Logger logger = LogManager.getLogger(UsersController.class);

    /**
     * Injected instance of {@link ValidationPatternService} to handle business logic
     */
    @Inject
    private ValidationPatternService validationPatternService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of all {@link ValidationPattern}s.
     */
    @Override
    public void refresh() {
        List<ValidationPattern> validationPatterns = validationPatternService.getByAdvanced(false);
        if (validationPatterns == null || validationPatterns.size() == 0) {
            logger.debug("ValidationPatterns couldn't be loaded. ValidationsController is empty.");
            this.entities = new ArrayList<>();
            super.displayMessageFromResource("error.summary.empty-validationpatterns",
              "error.detail.empty-validationpatterns", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.entities = validationPatterns;
    }

    /**
     * Get by ID method of ValidationPatternsController - not implemented
     * @param id the id
     * @return nothing
     */
    @Override
    public ValidationPattern getById(String id) {
        return null;
    }



}
