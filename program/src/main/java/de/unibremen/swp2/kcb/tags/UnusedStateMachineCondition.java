package de.unibremen.swp2.kcb.tags;

import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.service.ProcessStepService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

/**
 * Class UnusedStateMachineCondition
 *
 * @author Marc
 */
@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
        tagName = "stateMachineIsUnused", value = "de.unibremen.swp2.kcb.tags.UnusedStateMachineCondition")
public class UnusedStateMachineCondition extends UIComponentBase {

    /**
     * Logger object of the UnusedStateMachineConditional class
     */
    private static final Logger logger = LogManager.getLogger(UnusedStateMachineCondition.class);

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.UnusedStateMachineConditional";

    /**
     * CDI Instance of {@link ProcessStepService}
     */
    private ProcessStepService processStepService = getProcStepFacade();

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getFamily() {
        return FAMILY;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean isRendered() {
        StateMachine currentStateMachine = (StateMachine) getAttributes().get("machine");
        List<ProcessStep> allActiveSteps = processStepService.getActive();

        for(ProcessStep ps : allActiveSteps) {
            if(ps.getStateMachine().equals(currentStateMachine))
                return false;
        }

        return true;
    }

    /**
     * Retrieve BeanManager to manually access CDI Classes
     * @return the BeanManager
     */
    public BeanManager getBeanManager() {
        try {
            InitialContext initialContext = new InitialContext();
            return (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            logger.error("Couldn't get BeanManager through JNDI");
            return null;
        }
    }

    /**
     * Manually retrieve injected instance of {@link ProcessStepService}
     * @return injected instance of {@link ProcessStepService}
     */
    @SuppressWarnings("unchecked")
    public ProcessStepService getProcStepFacade()
    {
        BeanManager bm = getBeanManager();
        Bean<ProcessStepService> bean = (Bean<ProcessStepService>) bm.getBeans(ProcessStepService.class).iterator().next();
        CreationalContext<ProcessStepService> ctx = bm.createCreationalContext(bean);
        return (ProcessStepService) bm.getReference(bean, ProcessStepService.class, ctx);
    }
}
