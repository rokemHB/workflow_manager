package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.service.*;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.EntityAlreadyExistingException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a single {@link de.unibremen.swp2.kcb.model.ProcessStep}.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@Named
@ViewScoped
public class ProcessChainController extends SingleController<ProcessChain> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(ProcessChainController.class);

    /**
     * Injected instance of ValueService
     */
    @Inject
    private ValueService valueService;

    /**
     * Injected instance of StateHistoryService
     */
    @Inject
    private StateHistoryService stateHistoryService;

    /**
     * Injected instance of ProcedureService
     */
    @Inject
    private ProcedureService procedureService;

    /**
     * ProcessChain Service to handle actions
     */
    @Inject
    private ProcessChainService processChainService;

    /**
     * ProcessStep Service to handle actions
     */
    @Inject
    private ProcessStepService processStepService;

    /**
     * Class to handle drag and drop in frontend
     */
    @Getter
    @Setter
    private DualListModel<ProcessStep> selectedProcessSteps;

    /**
     * Setup method for ProcessChainController
     */
    @PostConstruct
    public void setup() {
        List<ProcessStep> allSteps = processStepService.getAll();
        List<ProcessStep> selectedProcessStepList = new ArrayList<>();

        this.entity = new ProcessChain();
        selectedProcessSteps = new DualListModel<>(allSteps, selectedProcessStepList);
    }

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        if (this.entity != null && selectedProcessSteps != null && selectedProcessSteps.getTarget() != null)
            this.entity.setChain(selectedProcessSteps.getTarget());
        try {
            processChainService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during ProcessChain creation.", e);
            final String processChainName = this.getSaveProcessChainName(entity);
            final String summary = localeController.formatString("error.summary.processchain-creation");
            final String detail = localeController.formatString("error.detail.processchain-creation");
            final String updatedDetail = detail != null ? detail.replace("<processChainName>", processChainName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("ProcessChainController with that name already exists.", e);
            final String processChainName = this.getSaveProcessChainName(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", processChainName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            processChainService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during ProcessChain deletion.", e);
            final String processChainName = this.getSaveProcessChainName(entity);
            final String summary = localeController.formatString("error.summary.processchain-deletion");
            final String detail = localeController.formatString("error.detail.processchain-deletion");
            final String updatedDetail = detail != null ? detail.replace("<processChainName>", processChainName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        if (this.entity != null && selectedProcessSteps != null && selectedProcessSteps.getTarget() != null)
            this.entity.setChain(selectedProcessSteps.getTarget());
        try {
            processChainService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during ProcessChain update.", e);
            final String processChainName = this.getSaveProcessChainName(entity);
            final String summary = localeController.formatString("error.summary.processchain-update");
            final String detail = localeController.formatString("error.detail.processchain-update");
            final String updatedDetail = detail != null ? detail.replace("<processChainName>", processChainName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Resets the entity with an empty ProcessChain
     */
    public void reset() {
        this.entity = new ProcessChain();
    }

    /**
     * Updates the entity with a given ProcessChain
     *
     * @param entity the ProcessChain to be updated
     */
    public void updateEntity(ProcessChain entity) {
        List<ProcessStep> allSteps = processStepService.getAll();
        this.entity = entity;
        this.selectedProcessSteps.setTarget(entity.getChain());
        this.selectedProcessSteps.setSource(this.intersect(allSteps, entity.getChain()));
        super.render("processChain_edit_form");
        super.render("processChain_start_form");
    }

    /**
     * Return a list containing all values of all that are not contained in the selected lsit.
     *
     * @param all      items to look for
     * @param selected items to remove from all items
     * @return items from all that are not contained in selected
     */
    private List<ProcessStep> intersect(final List<ProcessStep> all, final List<ProcessStep> selected) {
        ArrayList<ProcessStep> result = new ArrayList<>();
        for (final ProcessStep step : all) {
            if (!selected.contains(step))
                result.add(step);
        }
        return result;
    }

    /**
     * Get the name of the given entity and check for null references while doing so.
     *
     * @param entity to get name of
     * @return name if entity has one, 'null' otherwise
     */
    private String getSaveProcessChainName(final ProcessChain entity) {
        if (entity == null) return "null";
        if (entity.getName() == null) return "null";
        return entity.getName();
    }

    /**
     * Returns if the processChain can be deleted.
     *
     * @param processChain the process chain
     * @return the boolean
     */
    public boolean canDelete(ProcessChain processChain) {
        return processChainService.canDelete(processChain);
    }
}