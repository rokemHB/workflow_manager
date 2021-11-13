package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.controller.Controller;
import de.unibremen.swp2.kcb.model.GlobalConfig;
import de.unibremen.swp2.kcb.service.GlobalConfigService;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller class to handle {@link GlobalConfig}.
 *
 * @author Robin
 */
@ViewScoped
@Named
public class GlobalConfigController extends Controller {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(UserController.class);

    /**
     * GlobalConfigService to handle actions
     */
    @Inject
    private GlobalConfigService globalConfigService;

    /**
     * Old active Job days attribute as int for frontend validation
     */
    @Getter
    @Setter
    private int oldActiveJobDays;

    /**
     * Old active Job hours attribute as int for frontend validation
     */
    @Getter
    @Setter
    private int oldActiveJobHours;

    /**
     * Old active Job minutes attribute as int for frontend validation
     */
    @Getter
    @Setter
    private int oldActiveJobMinutes;

    /**
     * Set up method.
     */
    @PostConstruct
    public void setUp() {
        this.oldActiveJobDays = globalConfigService.getDays();
        this.oldActiveJobHours = globalConfigService.getHours();
        this.oldActiveJobMinutes = globalConfigService.getMinutes();
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    public void updateOldActiveJob() {
        try {
            globalConfigService.setOldActiveJobs(oldActiveJobDays,
                oldActiveJobHours, oldActiveJobMinutes);
        } catch (UpdateException e) {
            logger.debug("Error occurred during assembly creation.", e);
            final String summary = localeController.formatString("error.summary.oldactivejob-update");
            final String detail = localeController.formatString("error.detail.oldactivejob-update");
            displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
    }

}
