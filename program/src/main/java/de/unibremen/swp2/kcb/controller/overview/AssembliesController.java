package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.service.AssemblyService;
import de.unibremen.swp2.kcb.service.LocationService;
import de.unibremen.swp2.kcb.service.StockService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FilterAssembliesException;
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
 * Controller Class to handle a collection of {@link Assembly}s.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@ViewScoped
@Named("assembliesController")
public class AssembliesController extends OverviewController<Assembly> {

    /**
     * Logger object of the ProcessStepsController class
     */
    private static final Logger logger = LogManager.getLogger(AssembliesController.class);

    /**
     * Injected instance of {@link AssemblyService} to handle business logic
     */
    @Inject
    private AssemblyService assemblyService;

    /**
     * Injected instance of {@link LocationService} to handle business logic
     */
    @Inject
    private LocationService locationService;

    /**
     * Injected instance of {@link StockService} to handle business logic
     */
    @Inject
    private StockService stockService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of all {@link Assembly}s.
     */
    @Override
    public void refresh() {
        this.entities = new ArrayList<>();

        List<Assembly> assemblies = assemblyService.getAll();
        if (assemblies == null || assemblies.isEmpty()) {
            logger.debug("ProcessSteps couldn't be loaded. ProcessStepsController is empty.");
            super.displayMessageFromResource("error.summary.empty-assemblies",
                    "error.detail.empty-assemblies", FacesMessage.SEVERITY_ERROR);
            return;
        }

        this.entities = assemblies;
    }

    /**
     * Gets an assembly by its ID
     * @param id the id
     * @return the assembly
     */
    @Override
    public Assembly getById(String id) {
        try {
            return assemblyService.getById(id);
        } catch (InvalidIdException e) {
            logger.debug("Provided ProcessStep ID was found invalid.");
            final String idString = id;
            final String summary = localeController.formatString("error.summary.assemblies-idValidation");
            final String detail = localeController.formatString("error.detail.assemblies-idValidation");
            final String updatedDetail = detail != null ? detail.replace("<idString>", idString) : null;
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }

    /**
     * Return a collection of {@link Assembly}s, that are ready to transport to another {@link Workstation}.
     *
     * @return Collection of {@link Assembly}s.
     */
    public List<Assembly> getReadyForTransport() {
        List<Assembly> assemblies = assemblyService.getCollectable();

        if (assemblies.isEmpty()) {
            final String summary = localeController.formatString("error.summary.assemblies-readyForTransport");
            final String detail = localeController.formatString("error.detail.assemblies-readyForTransport");
            logger.debug("GetReadyForTransport is invalid.");
            super.displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }

        this.entities = assemblies;
        return this.entities;
    }

    /**
     * Returns all assemblies that are on carrier of a given carrierType.
     *
     * @param carrierType the carrierType
     * @return list of assemblies on that carrierType
     */
    public List<Assembly> getByCarrierType(CarrierType carrierType) {
        return assemblyService.getByCarrierType(carrierType);
    }

    /**
     * Find all Assemblies with a certain sample count
     *
     * @param sampleCount the sample count to be searched
     * @return all Assemblies with that sample count
     */
    List<Assembly> getBySampleCount(int sampleCount) throws FindByException {
        List<Assembly> assemblies = new ArrayList<>();
        try {
            assemblies = assemblyService.getBySampleCount(sampleCount);
        } catch (FindByException e) {
            final String summary = localeController.formatString("error.summary.assemblies-bySampleCount");
            final String detail = localeController.formatString("error.detail.assemblies-bySampleCount");
            logger.debug("Given sampleCount is invalid.");
            super.displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
        this.entities = assemblies;
        return this.entities;
    }

    /**
     * Find all Assemblies with a certain alloy
     *
     * @param alloy the alloy to be searched
     * @return all Assemblies with that alloy
     */
    List<Assembly> getByAlloy(String alloy) {
        List<Assembly> assemblies = new ArrayList<>();
        try {
            assemblies = assemblyService.getByAlloy(alloy);
        } catch (FindByException e) {
            final String summary = localeController.formatString("error.summary.assemblies-byAlloy");
            final String detail = localeController.formatString("error.detail.assemblies-byAlloy");
            logger.debug("Given alloy is invalid.");
            super.displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
        this.entities = assemblies;
        return this.entities;
    }

    /**
     * Gets all in stock.
     *
     * @return the all in stock
     */
    public List<Assembly> getAllInStock() {
        try {
            return stockService.getAllInStock();
        } catch (FindByException e) {
            final String summary = localeController.formatString("error.summary.assemblies-inStock");
            final String detail = localeController.formatString("error.detail.assemblies-inStock");
            logger.debug("No Assemblies found in Stocks.");
            super.displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }

    /**
     * Returns all Assemblies from a given list of Assemblies that are currently not in an active Job
     *
     * @param asses the given List to be filtered of
     * @return a list of Assemblies which are not in active Jobs
     */
    public List<Assembly> filterNotInActiveJob(final List<Assembly> asses) {
        List<Assembly> result = new ArrayList<>();
        try {
            result = assemblyService.filterNotInActiveJob(asses);
        } catch (FilterAssembliesException e) {
            final String summary = localeController.formatString("error.summary.assemblies-not-found-in-inactive-jobs");
            final String detail = localeController.formatString("error.detail.assemblies-not-found-in-inactive-jobs");
            logger.debug("Could not filter any Assemblies in inactive Jobs. {}", e.getMessage());
            super.displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
        return result;
    }
}
