package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.service.ProcessChainService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a collection of {@link ProcessChain}s.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@RequestScoped
@Named("processchainsController")
public class ProcessChainsController extends OverviewController<ProcessChain> {

    /**
     * Logger object of the ProcessChainsController class
     */
    private static final Logger logger = LogManager.getLogger(ProcessChainsController.class);

    /**
     * Injected instance of {@link ProcessChainService} to handle business logic
     */
    @Inject
    private ProcessChainService processChainService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }


    /**
     * Return a collection of all {@link ProcessChain}s.
     *
     * @return Collection of all {@link ProcessChain}s.
     */
    public List<ProcessChain> getAll() {
        this.entities = new ArrayList<>();
        logger.debug("Querying all stored ProcessChains.");

        this.entities = processChainService.getAll();
        return this.entities;
    }

    /**
     * Refresh the collection of all {@link ProcessChain}s.
     */
    @Override
    public void refresh() {
        List<ProcessChain> processChains = processChainService.getAll();

        if (processChains == null || processChains.isEmpty()) {
            logger.debug("ProcessChains couldn't be loaded. ProcessChainsController is empty.");
            this.entities = new ArrayList<>();
            return;
        }
        this.entities = processChains;
    }

    /**
     * Get ProcessChain by ID
     *
     * @param id the ID
     * @return ProcessChain with that ID
     */
    @Override
    public ProcessChain getById(String id) {
        try {
            return processChainService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.processchain-by-id-not-found",
                    "error.detail.processchain-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }

    /**
     * Find all ProcessChains by its 'chain'-attribute,
     * which consists of a List of ProcessStep
     *
     * @param chain the chain to be searched for
     * @return all ProcessChains with that chain
     * @see ProcessStep
     */
    List<ProcessChain> getByChain(List<ProcessStep> chain) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying ProcessChains by chain");
            this.entities = processChainService.getByChain(chain);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.processchain-by-chain-not-found",
                    "error.detail.processchain-by-chain-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }
}