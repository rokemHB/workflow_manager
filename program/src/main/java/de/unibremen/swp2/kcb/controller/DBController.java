package de.unibremen.swp2.kcb.controller;

import de.unibremen.swp2.kcb.service.DBService;
import de.unibremen.swp2.kcb.service.serviceExceptions.BackupException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class DBController
 *
 * @author Marius
 */
@Named("dbController")
@ViewScoped
public class DBController extends Controller {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(DBController.class);

    /**
     * DBService to handle DB related tasks
     */
    @Inject
    private DBService dbService;

    /**
     * Return the backup file as StreamedContent for PrimeFaces
     *
     * @return StreamedContent of backup file
     */
    public StreamedContent getBackupFile() {
        File tmpFile = null;
        try {
            tmpFile = this.dbService.backup();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
            return new DefaultStreamedContent(new FileInputStream(tmpFile), "application/zip", "KCB_BACKUP_" + LocalDateTime.now().format(formatter));
        } catch (BackupException e) {
            logger.debug("BackupException occurred {}", e.getMessage());
            super.displayMessageFromResource("error.summary.download-failed", "error.detail.download-failed", FacesMessage.SEVERITY_ERROR);
        } catch (FileNotFoundException e) {
            if (tmpFile != null)
                logger.debug("FileNotFoundException occurred for {}", tmpFile.getName());
            else
                logger.debug("FileNotFoundException occurred. File was null. {}", e.getMessage());
            super.displayMessageFromResource("error.summary.download-failed", "error.detail.download-failed", FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }
}
