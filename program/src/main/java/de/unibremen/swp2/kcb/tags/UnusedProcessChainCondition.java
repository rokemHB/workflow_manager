package de.unibremen.swp2.kcb.tags;

import de.unibremen.swp2.kcb.model.ProcessChain;
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
 * Class UnusedProcessChainCondition
 *
 * @author Marc
 */
@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
        tagName = "chainIsUnused", value = "de.unibremen.swp2.kcb.tags.UnusedProcessChainCondition")
public class UnusedProcessChainCondition extends UIComponentBase {

    /**
     * Logger object of the UnusedProcessChainConditional class
     */
    private static final Logger logger = LogManager.getLogger(UnusedProcessChainCondition.class);

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.UnusedProcessChainCondition";

    /**
     * CDI Instance of {@link ProcessChainService}
     */
    private ProcessChainService processChainService = getProcChainFacade();

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
        ProcessChain currentChain = (ProcessChain) getAttributes().get("chain");
        List<ProcessChain> allActiveChains = processChainService.getActive();

        return !allActiveChains.contains(currentChain);
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
