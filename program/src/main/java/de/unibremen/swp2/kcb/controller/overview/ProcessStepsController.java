package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.service.ProcessStepService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
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
 * Controller class to handle a collection of {@link ProcessStep}s.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@ViewScoped
@Named("processStepsController")
public class ProcessStepsController extends OverviewController<ProcessStep> {

    /**
     * Logger object of the ProcessStepsController class
     */
    private static final Logger logger = LogManager.getLogger(ProcessStepsController.class);

    /**
     * Injected instance of {@link ProcessStepService} to handle business logic
     */
    @Inject
    private ProcessStepService processStepService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of all {@link ProcessStep}s.
     */
    @Override
    public void refresh() {
        List<ProcessStep> processSteps = processStepService.getAll();
        this.entities = new ArrayList<>();

        if (processSteps == null || processSteps.isEmpty()) {
            logger.debug("ProcessSteps couldn't be loaded. ProcessStepsController is empty.");
            super.displayMessageFromResource("error.summary.empty-processSteps",
                    "error.detail.empty-processSteps", FacesMessage.SEVERITY_ERROR);
        } else {
            this.entities = processSteps;
        }
    }

    /**
     * Get ProcessStep by ID
     *
     * @param id the ID
     * @return ProcessStep with that ID
     */
    @Override
    public ProcessStep getById(String id) {
        ProcessStep result = null;
        try {
            result = processStepService.getById(id);
        } catch (InvalidIdException e) {
            logger.debug("Provided ProcessStep ID was found invalid.");
            final String summary = localeController.formatString("error.summary.processSteps-idValidation");
            final String detail = localeController.formatString("error.detail.processSteps-idValidation");
            final String updatedDetail = detail != null ? detail.replace("<idString>", id) : null;
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        return result;
    }

    /**
     * Find all ProcessSteps with a certain name
     *
     * @param name the name to be searched
     * @return all ProcessSteps with that name
     */
    public List<ProcessStep> getByName(String name) {
        List<ProcessStep> processSteps = new ArrayList<>(); // to return empty list when failed
        try {
            processSteps = processStepService.getByName(name);
        } catch (FindByException e) {
            final String summary = localeController.formatString("error.summary.processSteps-nameValidation");
            final String detail = localeController.formatString("error.detail.processSteps-nameValidation");
            final String updatedDetail = detail != null ? detail.replace("<name>", name) : null;
            logger.debug("Given name is invalid.");
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        this.entities = processSteps;
        return this.entities;
    }

    /**
     * Find all ProcessSteps with a certain estimated Duration
     *
     * @param estDuration the estimated Duration to be searched
     * @return all ProcessSteps with that estimated Duration
     */
    public List<ProcessStep> getByEstDuration(int estDuration) {
        List<ProcessStep> processSteps = new ArrayList<>();
        try {
            processSteps = processStepService.getByEstDuration(estDuration);
        } catch (FindByException e) {
            final String dur = Integer.toString(estDuration);
            final String summary = localeController.formatString("error.summary.processSteps-estDurationValidation");
            final String detail = localeController.formatString("error.detail.processSteps-estDurationValidation");
            final String updatedDetail = detail != null ? detail.replace("<dur>", dur) : null;
            logger.debug("Given estimatedDuration is negativ.");
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        this.entities = processSteps;
        return this.entities;
    }

    /**
     * Find all ProcessSteps with a certain StateMachine
     *
     * @param stateMachine the StateMachine to be searched
     * @return all ProcessSteps with that StateMachine
     * @see StateMachine
     */
    public List<ProcessStep> getByStateMachine(StateMachine stateMachine) {
        List<ProcessStep> processSteps = new ArrayList<>();
        try {
            processSteps = processStepService.getByStateMachine(stateMachine);
        } catch (FindByException e) {
            final String smName = stateMachine.getName();
            final String summary = localeController.formatString("error.summary.processSteps-stateMachineValidation");
            final String detail = localeController.formatString("error.detail.processSteps-stateMachineValidation");
            final String updatedDetail = detail != null ? detail.replace("<smName>", smName) : null;
            logger.debug("Given stateMachine is invalid.");
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        this.entities = processSteps;
        return this.entities;
    }

    /**
     * Find all ProcessSteps with a certain Workstation
     *
     * @param workstation the Workstation to be searched
     * @return all ProcessSteps with that Workstation
     * @see Workstation
     */
    public List<ProcessStep> getByWorkstation(Workstation workstation) {
        List<ProcessStep> processSteps = new ArrayList<>();
        try {
            processSteps = processStepService.getByWorkstation(workstation);
        } catch (FindByException e) {
            final String wsName = workstation.getName();
            final String summary = localeController.formatString("error.summary.processSteps-workstationValidation");
            final String detail = localeController.formatString("error.detail.processSteps-workstationValidation");
            final String updatedDetail = detail != null ? detail.replace("<wsName>", wsName) : null;
            logger.debug("Given workstation is invalid.");
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        this.entities = processSteps;
        return this.entities;
    }

    /**
     * Find all ProcessSteps with a certain Parameter
     *
     * @param parameter the Parameter to be searched
     * @return all ProcessSteps with that Parameter
     * @see Parameter
     */
    public List<ProcessStep> getByParameter(Parameter parameter) {
        List<ProcessStep> processSteps = new ArrayList<>();
        try {
            processSteps = processStepService.getByParameter(parameter);
        } catch (FindByException e) {
            final String pmName = parameter.getField();
            final String summary = localeController.formatString("error.summary.processSteps-parameterValidation");
            final String detail = localeController.formatString("error.detail.processSteps-parameterValidation");
            final String updatedDetail = detail != null ? detail.replace("<pmName>", pmName) : null;
            logger.debug("Given parameter is invalid.");
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        return processSteps;
    }

    /**
     * Find all ProcessSteps with a certain CarrierType, considering both preparation and output
     * carrierTypes
     *
     * @param carrierType the CarrierType to be searched
     * @return all ProcessSteps with that CarrierType
     * @see CarrierType
     */
    public List<ProcessStep> getByCarrierType(CarrierType carrierType) {
        List<ProcessStep> processSteps = new ArrayList<>();
        processSteps.addAll(getByOutputCarrierType(carrierType));
        processSteps.addAll(getByPreparationCarrierType(carrierType));
        this.entities = processSteps;
        return this.entities;
    }

    /**
     * Find all ProcessSteps with a certain output-CarrierType
     *
     * @param output the output-CarrierType to be searched
     * @return all ProcessSteps with that CarrierType
     * @see CarrierType
     */
    public List<ProcessStep> getByOutputCarrierType(CarrierType output) {
        List<ProcessStep> processSteps = new ArrayList<>();
        try {
            processSteps = processStepService.getByOutputCarrierType(output);
        } catch (FindByException e) {
            final String outName = output.getName();
            final String summary = localeController.formatString("error.summary.processSteps-outputCarrierTypeValidation");
            final String detail = localeController.formatString("error.detail.processSteps-outputCarrierTypeValidation");
            final String updatedDetail = detail != null ? detail.replace("<outName>", outName) : null;
            logger.debug("Given output-Carriertype is invalid.");
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        return processSteps;
    }

    /**
     * Find all ProcessSteps with a certain preparation-CarrierType
     *
     * @param preparation the preparation-CarrierType to be searched
     * @return all ProcessSteps with that CarrierType
     * @see CarrierType
     */
    public List<ProcessStep> getByPreparationCarrierType(CarrierType preparation) {
        List<ProcessStep> processSteps = new ArrayList<>();
        try {
            processSteps = processStepService.getByPreparationCarrierType(preparation);
        } catch (FindByException e) {
            final String prepName = preparation.getName();
            final String summary = localeController.formatString("error.summary.processSteps-preparatiomCarrierTypeValidation");
            final String detail = localeController.formatString("error.detail.processSteps-preparatiomCarrierTypeValidation");
            final String updatedDetail = detail != null ? detail.replace("<prepName>", prepName) : null;
            logger.debug("Given preparation-Carriertype is invalid.");
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        return processSteps;
    }

    /**
     * Return the collection of all existing {@link ProcessStep}s.
     *
     * @return All existing {@link ProcessStep}s.
     */
    public List<ProcessStep> getAll() {
        this.entities = new ArrayList<>();
        logger.debug("Querying all stored ProcessSteps.");

        this.entities = processStepService.getAll();
        return this.entities;
    }

    /**
     * Return Parameters as a space seperated string
     *
     * @param processStep to return parameters of
     * @return String version of the ProcessSteps parameters
     */
    public String getParameterString(ProcessStep processStep) {
        StringBuilder result = new StringBuilder();
        for (Parameter param : processStep.getParameters()) {
            result.append(param.getField());
            result.append(" ");

        }
        return result.toString();
    }
}