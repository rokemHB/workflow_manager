package de.unibremen.swp2.kcb.service.serviceExceptions;

import de.unibremen.swp2.kcb.validator.backend.ValidationException;

/**
 * Class for CreatesProcessStepNotFirstException
 *
 * @author Arvid
 */
public class CreatesProcessStepNotFirstException extends ValidationException {
    public CreatesProcessStepNotFirstException(String s) {
        super(s);
    }
}
