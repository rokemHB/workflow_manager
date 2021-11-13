package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.persistence.locations.StockRepository;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to handle Stocks.
 *
 * @author Marc
 * @author Robin
 * @author Arvid
 */
@Transactional
@KCBSecure
public class StockService implements Service<Stock> {

    /**
     * Logger object of the Stock Service class
     */
    private static final Logger logger = LogManager.getLogger(de.unibremen.swp2.kcb.service.StockService.class);

    /**
     * Injected instance of {@link StockRepository}
     */
    @Inject
    private StockRepository stockRepository;

    /**
     * Injected instance of {@link LocationService}
     */
    @Inject
    private LocationService locationService;

    /**
     * Injected instance of {@link UserService} to validate provided {@link Stock}s.
     */
    @Inject
    private UserService userService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return Stock that has been created and stored
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    @RequiresAuthentication
    public Stock create(final Stock entity) throws CreationException {

        //Saving stock
        Stock repoEntity;
        try {
            logger.trace("Attempting to save stock \"{}\" ...", entity);
            repoEntity = stockRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting stock \"{}\". Can't create stock.", entity);
            throw new CreationException("Can't create stock: " + e.getMessage());
        }

        logger.trace("Saving of stock \"{}\" completed without exceptions.", entity);
        logger.info("Create stock \"{}\" - triggered by: {}", entity.getPosition(), userService.getExecutingUser().getUsername());
        logger.trace("Returning stock \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Updates the stored entity with the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @see de.unibremen.swp2.kcb.validator.backend
     */
    @Override
    @RequiresAuthentication
    public Stock update(final Stock entity) throws UpdateException {

        //Checking, if stock is stored in database
        final Stock storedStock = stockRepository.findBy(entity.getId());
        if (storedStock == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving stock
        Stock repoEntity;
        try {
            logger.trace("Attempting to save new stock \"{}\" ...", entity);
            repoEntity = stockRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting stock \"{}\". Can't update stock.", entity);
            throw new UpdateException("Can't update stock: " + e.getMessage());
        }

        logger.trace("Saving of stock \"{}\" completed without exceptions.", entity);
        logger.info("Update stock \"{}\" - triggered by: {}", entity.getPosition(), userService.getExecutingUser().getUsername());
        logger.trace("Returning stock \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     */
    @Override
    @RequiresAuthentication
    public void delete(final Stock entity) throws DeletionException {

        //Checking, if stock is stored in database
        final Stock storedStock = stockRepository.findBy(entity.getId());
        if (storedStock == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting stock
        try {
            logger.trace("Attempting to delete stock \"{}\" ...", entity);
            stockRepository.attachAndRemove(entity);
            logger.info("Delete stock \"{}\" triggered by: {}", entity.getPosition(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting stock \"{}\". Can't delete stock.", entity);
            throw new DeletionException("Can't delete stock: " + e.getMessage());
        }
    }

    /**
     * Return the Stock with the provided unique ID
     *
     * @param id to be searched for
     * @return one specific Stock
     * @throws InvalidIdException if the given ID was invalid
     */
    @RequiresAuthentication
    public Stock getById(String id) throws InvalidIdException {

        //Validating stockID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "StockID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("StockID \"{}\" is valid.", id);

        //Finding stock by ID
        final Stock entity;
        try {
            logger.trace("Attempting to find stock by ID \"{}\" ...", id);
            entity = stockRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding stock by ID \"{}\". Can't find stock.", id);
            throw new InvalidIdException("Can't find stock: " + e.getMessage());
        }

        logger.trace("Finding of stock by ID \"{}\" completed without exceptions.", id);
        logger.info("Find stock by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning stock by ID \"{}\"", id);
        return entity;
    }

    /**
     * Gets all in stock.
     *
     * @return the all in stock
     */
    public List<Assembly> getAllInStock() throws FindByException {
        List<Assembly> allInStocks = new ArrayList<>();

        for(Stock s : this.getAll()) {
            allInStocks.addAll(locationService.getAllAssembliesAt(s));
        }

        return allInStocks;
    }

    /**
     * Return all existing {@link Stock}s
     *
     * @return Collection of all existing Stocks.
     */
    @RequiresAuthentication
    public List<Stock> getAll() {
        logger.info("Querying database for all existing Stocks.");

        List<Stock> allStocks;
        allStocks = stockRepository.findAll();

        if (allStocks.isEmpty()) {
            logger.debug("No Stocks found.");
        }

        logger.info("Successful query: Found {} total Stocks.", allStocks.size() );
        return allStocks;
    }
}