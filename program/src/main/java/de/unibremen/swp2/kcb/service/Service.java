package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Generic Interface to be extended by services. Makes available basic functionality of services.
 *
 * @param <T> Object to be handled by the service.
 *            T should be an instance of a class specified in the model package.
 * @see de.unibremen.swp2.kcb.model
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
public interface Service<T> extends Serializable {

    /**
     * Logger object of the LocaleController class
     */
    Logger logger = LogManager.getLogger(Service.class);

    /**
     * Creates an entity and stores it in the system after successful validation of the entity.
     *
     * @param entity to be created and persisted
     * @return created entity
     */
    T create(T entity) throws CreationException, EntityAlreadyExistingException;

    /**
     * Updates an entity stored in the system after successful validation of the entity.
     *
     * @param entity to be updated
     * @return updated entity
     */
    T update(T entity) throws UpdateException;

    /**
     * Deletes an entity from the system.
     *
     * @param entity to be deleted
     */
    void delete(T entity) throws DeletionException;

    /**
     * Verifies provided ID and makes sure it is not null or empty.
     *
     * @param id to be checked
     * @throws InvalidIdException Thrown if the ID was invalid
     */
    default void checkId(String id) throws InvalidIdException {
        if (id == null || id.equals("")) {
            throw new InvalidIdException("Provided ID was not valid.");
        }
    }

    /**
     * Create a temporary file with the given content.
     *
     * @param content to be written to the temp file
     * @param prefix  prefix for the created temp file name
     * @param suffix  suffix for the created temp file name
     * @return File containing the content specified as parameter
     * @throws IOException if file handling failes
     */
    default File createTmpFile(final String content, final String prefix, final String suffix) throws IOException {
        File tmpFile = null;
        BufferedWriter writer = null;
        try {
            tmpFile = File.createTempFile("parameters", ".json");

            writer = new BufferedWriter(new FileWriter(tmpFile.getAbsoluteFile()));
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            logger.debug("Writing tmp file failed.");
        } finally {
            if (writer != null) writer.close();
        }
        return tmpFile;
    }
}
