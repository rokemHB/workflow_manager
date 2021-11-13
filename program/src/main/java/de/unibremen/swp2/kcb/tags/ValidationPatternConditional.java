package de.unibremen.swp2.kcb.tags;

import de.unibremen.swp2.kcb.service.ValidationPatternService;
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
 * Class ValidationPatternConditional
 *
 * @author Robin
 */
@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
  tagName = "validationEditDisabled", value = "de.unibremen.swp2.kcb.tags.ValidationPatternConditional")
public class ValidationPatternConditional extends UIComponentBase {

    /**
     * Logger object of the ValidationPatternConditional class
     */
    private static final Logger logger = LogManager.getLogger(ValidationPatternConditional.class);

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.ValidationPatternConditional";

    /**
     * CDI Instance of {@link ValidationPatternService}
     */
    private ValidationPatternService validationPatternService = getValPatFacade();

    /**
     * Return family of this UIComponent
     *
     * @return the family
     */
    @Override
    public String getFamily() {
        return FAMILY;
    }

    /**
     * Checks whether the component should be rendered.
     * @return whether it should be rendered
     */
    @Override
    public boolean isRendered() {
        logger.trace("checking for environmental variable KCB_PATTERN");
        return validationPatternService.isEnabled();
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
     * Manually retrieve injected instance of ValidationPatterns
     * @return injected instance of Validationpatterns
     */
    @SuppressWarnings("unchecked")
    public ValidationPatternService getValPatFacade()
    {
        BeanManager bm = getBeanManager();
        Bean<ValidationPatternService> bean = (Bean<ValidationPatternService>) bm.getBeans(ValidationPatternService.class).iterator().next();
        CreationalContext<ValidationPatternService> ctx = bm.createCreationalContext(bean);
        return (ValidationPatternService) bm.getReference(bean, ValidationPatternService.class, ctx);
    }

}
