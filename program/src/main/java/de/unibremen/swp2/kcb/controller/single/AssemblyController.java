package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.service.AssemblyService;
import de.unibremen.swp2.kcb.service.CarrierService;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;

/**
 * Controller Class to handle a single {@link Assembly}.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@ViewScoped
@Named("assemblyController")
public class AssemblyController extends SingleController<Assembly> {

    /**
     * Logger object of the AssemblyController class
     */
    private static final Logger logger = LogManager.getLogger(AssemblyController.class);

    /**
     * Injected instance of {@link AssemblyService}.
     */
    @Inject
    private AssemblyService assemblyService;

    /**
     * Injected instance of {@link CarrierService}.
     */
    @Inject
    private CarrierService carrierService;

    /**
     * Sends a message to an {@link de.unibremen.swp2.kcb.model.Role#ADMIN} to notify about a lost {@link Assembly}.
     */
    public void notifyLoss() {
        try {
            assemblyService.notifyLoss(this.entity);
        } catch (NotifyLossException e) {
            logger.debug("Error occurred during notify the assembly as lost.", e);
            final String assembly = entity != null ? entity.toString() : "";
            final String summary = localeController.formatString("error.summary.assembly-notifyLoss");
            final String detail = localeController.formatString("error.detail.assembly-notifyLoss");
            final String updatedDetail = detail != null ? detail.replace("<assembly>", assembly) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            assemblyService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during assembly creation.", e);
            final String assembly = entity != null ? entity.toString() : "";
            final String summary = localeController.formatString("error.summary.assembly-creation");
            final String detail = localeController.formatString("error.detail.assembly-creation");
            final String updatedDetail = detail != null ? detail.replace("<assembly>", assembly) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            assemblyService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during assembly deletion.", e);
            final String assembly = entity != null ? entity.toString() : "";
            final String summary = localeController.formatString("error.summary.assembly-deletion");
            final String detail = localeController.formatString("error.detail.assembly-deletion");
            final String updatedDetail = detail != null ? detail.replace("<assembly>", assembly) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            assemblyService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during assembly update.", e);
            final String assembly = entity != null ? entity.toString() : "";
            final String summary = localeController.formatString("error.summary.assembly-update");
            final String detail = localeController.formatString("error.detail.assembly-update");
            final String updatedDetail = detail != null ? detail.replace("<assembly>", assembly) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Split by size 1.
     */
    public void splitBySize1() {
        try {
            assemblyService.splitBySize(this.entity, 1);
        } catch (SplitException e) {
            logger.debug("Error occurred during split assembly.", e);
            final String assembly = this.entity.getAssemblyID();
            final String summary = localeController.formatString("error.summary.assembly-split");
            final String detail = localeController.formatString("error.detail.job-update");
            final String updatedDetail = detail != null ? detail.replace("<assembly>", assembly) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);

        }
    }

    /**
     * Reset Assembly.
     */
    public void reset() {
        this.entity = new Assembly();
    }

    /**
     * Update Assembly entity.
     *
     * @param entity the entity
     */
    public void updateEntity(Assembly entity) {
        this.entity = entity;
        super.render("assembly_edit_form");
    }

    /**
     * Handle f:selectItems from workstations Facelet.
     *
     * @param ids of selected users
     */
    public void setCarriers(String[] ids) {
        this.entity.setCarriers(carrierService.getByIds(Arrays.asList(ids)));
    }

    /**
     * Method is needed to prevent flame from jsf.
     *
     * @return the carriers
     */
    public String[] getCarriers() {
        // LEAVE ME HERE :)
        return new String[0];
    }
}