package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.UserRepository;
import de.unibremen.swp2.kcb.persistence.locations.TransportRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * Class TransportService
 *
 * @author Robin
 * @author Arvid
 */
@Transactional
public class TransportService implements Serializable {

    /**
     * Logger object of the Transport Service class
     */
    private static final Logger logger = LogManager.getLogger(de.unibremen.swp2.kcb.service.TransportService.class);

    /**
     * Injected instance of {@link TransportRepository}
     */
    @Inject
    private TransportRepository transportRepository;

    /**
     * Injected instance of {@link UserRepository}
     */
    @Inject
    private UserRepository userRepository;

    /**
     * Injected instance of {@link UserService} to validate provided {@link Transport}s.
     */
    @Inject
    private UserService userService;

    /**
     * Gets all transports.
     *
     * @return the all
     */
    public List<Transport> getAll() {
        return transportRepository.findAll();
    }

    /**
     * Gets transport by user.
     *
     * @param user the user
     * @return the transport by user
     */
    public List<Transport> getTransportByUser(User user) {
        return transportRepository.findByTransporter(user);
    }

    /**
     * Return2 the {@link Transport} with the given id.
     *
     * @param id of the Transport
     * @return Transport with the given id
     * @throws InvalidIdException thrown if validating given ID fails
     */
    public Transport getById(String id) throws InvalidIdException {

        //Finding transport by ID
        final Transport entity;
        try {
            logger.trace("Attempting to find transport by ID \"{}\" ...", id);
            entity = transportRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding transport by ID \"{}\". Can't find transport.", id);
            throw new InvalidIdException("Can't find transport: " + e.getMessage());
        }

        logger.trace("Finding of transport by ID \"{}\" completed without exceptions.", id);
        logger.info("Find transport by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning transport by ID \"{}\"", id);
        return entity;
    }

    /**
     * Creates a new transport for a user with the role transport.
     *
     * @param user the user
     */
    public void create(User user) {
        if (user.getRoles().contains(Role.TRANSPORT)) {
            Transport transport = new Transport();
            transport.setTransporter(user);
            transport.setPosition(user.getFirstName() + " " + user.getLastName());
            transportRepository.save(transport);
        }
    }

    /**
     * Updates the transport of the current user.
     * @param user the user
     */
    public void update(User user) {
        Transport transport = this.getByUser(user);

        if (transport != null && user.getRoles().contains(Role.TRANSPORT)) {
            transport.setPosition(user.getFirstName() + " " + user.getLastName());
            transportRepository.saveAndFlushAndRefresh(transport);
        } else if (transport != null && !user.getRoles().contains(Role.TRANSPORT)) {
            transportRepository.attachAndRemove(transport);
        } else if (transport == null && user.getRoles().contains(Role.TRANSPORT)) {
            this.create(user);
        }
    }

    /**
     * Deletes the transport for a user.
     * @param user the user
     */
    public void delete(User user) throws DeletionException {
        if (user.getRoles().contains(Role.TRANSPORT)) {
            Transport storedTransport = transportRepository.findByTransporter(user).get(0);
            transportRepository.attachAndRemove(storedTransport);
        }
    }

    /**
     * Returns the Transport for a given user. Returns null if there is no transport.
     *
     * @param user the user
     * @return the by user
     */
    public Transport getByUser(User user) {
        if (user == null)
            return null;

        List<Transport> transport = transportRepository.findByTransporter(user);

        if (transport == null || transport.isEmpty())
            return null;

        return transport.get(0);
    }
}
