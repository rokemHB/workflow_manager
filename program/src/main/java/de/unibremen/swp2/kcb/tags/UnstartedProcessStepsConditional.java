package de.unibremen.swp2.kcb.tags;

import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.service.ProcessChainService;
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
 * Class UnstartedProcessStepsConditional
 *
 * @author Marc
 * @author Robin
 */
@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
        tagName = "stepIsUnstarted", value = "de.unibremen.swp2.kcb.tags.UnstartedProcessStepsConditional")
public class UnstartedProcessStepsConditional extends UIComponentBase {

    /**
     * Logger object of the UnstartedProcessStepsConditional class
     */
    private static final Logger logger = LogManager.getLogger(UnstartedProcessStepsConditional.class);

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.UnstartedProcessStepsConditional";

    /**
     * CDI Instance of {@link ProcessChainService}
     */
    private ProcessChainService processChainService = getProcChainFacade();

    /**
     * Returns the FAMILY of the UIComponent
     * @return the FAMILY of the UICmponent
     */
    @Override
    public String getFamily() {
        return FAMILY;
    }

    /**
     * Checks whether the component should be rendered
     * @return whether object should be rendered
     */
    @Override
    public boolean isRendered() {
        ProcessStep currentStep = (ProcessStep) getAttributes().get("step");
        List<ProcessChain> chains = processChainService.getActive();

        for(ProcessChain pc : chains) {
            if(pc.getChain().contains(currentStep)) {
                return false;
            }
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
     * Manually retrieve injected instance of ProcessChainService
     * @return injected instance of ProcessChainService
     */
    @SuppressWarnings("unchecked")
    public ProcessChainService getProcChainFacade()
    {
        BeanManager bm = getBeanManager();
        Bean<ProcessChainService> bean = (Bean<ProcessChainService>) bm.getBeans(ProcessChainService.class).iterator().next();
        CreationalContext<ProcessChainService> ctx = bm.createCreationalContext(bean);
        return (ProcessChainService) bm.getReference(bean, ProcessChainService.class, ctx);
    }
}