package de.unibremen.swp2.kcb.service.serviceExceptions;

import de.unibremen.swp2.kcb.validator.backend.ValidationException;

/**
 * Class for DeletesProcessStepNotLastException
 *
 * @author Arvid
 */
public class DeletesProcessStepNotLastException extends ValidationException {
    public DeletesProcessStepNotLastException(String s) {
        super(s);
    }
}
