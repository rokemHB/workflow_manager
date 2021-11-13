package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreatesProcessStepNotFirstException;

import java.util.regex.Pattern;

/**
 * Validator to validate ProcessChains.
 *
 * @see ProcessChain
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 */
public class ProcessChainValidator extends PatternValidator<ProcessChain> {
    /**
     * Validates a ProcessChain
     *
     * @param instance ProcessChain to be validated
     * @return true if ProcessChain is valid, false if ProcessChain is invalid
     */
    @Override
    public boolean validate(ProcessChain instance) throws ValidationException {
        if (instance == null)
            throw new ValidationNullPointerException("Entered instance was null");
        if (instance.getChain() == null)
            throw new ValidationNullPointerException("Chain of instance " + instance.toString() + " is null.");
        if (instance.getName() == null)
            throw new ValidationNullPointerException("Name of instance " + instance.toString() + " is null.");

        if (instance.getChain().isEmpty())
            throw new EmptyProcessChainException("Can't create ProcessChain " + instance.toString() + " with empty Chain");

        //Validate creating processSteps in processChain
        for (ProcessStep processStep : instance.getChain()) {
            if (processStep.isCreates() && instance.getChain().indexOf(processStep) != 0)
                throw new CreatesProcessStepNotFirstException("Creating ProcessStep must be the first ProcessStep.");
        }

        //Validate deleting processSteps in processChain
        for (ProcessStep processStep : instance.getChain()) {
            if (processStep.isDeletes() && instance.getChain().indexOf(processStep) != instance.getChain().size() - 1)
                throw new CreatesProcessStepNotFirstException("Deleting ProcessStep must be the first ProcessStep.");
        }

        final Pattern pattern = validatorConfig.getPattern("ProcessChainName");

        return checkMatch(pattern, instance.getName());
    }
}
