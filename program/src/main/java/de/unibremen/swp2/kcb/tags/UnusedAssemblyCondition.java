package de.unibremen.swp2.kcb.tags;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.service.AssemblyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Class UnusedAssemblyCondition
 *
 * @author Arvid
 */
@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
        tagName = "assemblyIsUnused", value = "de.unibremen.swp2.kcb.tags.UnusedAssemblyCondition")
public class UnusedAssemblyCondition extends UIComponentBase {

    /**
     * Logger object of the UnusedAssemblyConditional class
     */
    private static final Logger logger = LogManager.getLogger(UnusedAssemblyCondition.class);

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.UnusedAssemblyCondition";

    /**
     * CDI Instance of {@link de.unibremen.swp2.kcb.model.Assembly}
     */
    private AssemblyService assemblyService = getAssemblyFacade();

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
        Assembly currentAssembly = (Assembly) getAttributes().get("assembly");
        return !assemblyService.isInUse(currentAssembly);
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
     * Manually retrieve injected instance of AssemblyService
     * @return injected instance of AssemblyService
     */
    @SuppressWarnings("unchecked")
    public AssemblyService getAssemblyFacade()
    {
        BeanManager bm = getBeanManager();
        Bean<AssemblyService> bean = (Bean<AssemblyService>) bm.getBeans(AssemblyService.class).iterator().next();
        CreationalContext<AssemblyService> ctx = bm.createCreationalContext(bean);
        return (AssemblyService) bm.getReference(bean, AssemblyService.class, ctx);
    }
}
