package de.unibremen.swp2.kcb.tags;

import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.service.CarrierService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
        tagName = "canDeleteCarrier", value = "de.unibremen.swp2.kcb.tags.CanDeleteCarrierCondition")
public class CanDeleteCarrierCondition extends UIComponentBase {

    /**
     * Logger object of the CanDeleteCarrierCondition class
     */
    private static final Logger logger = LogManager.getLogger(CanDeleteCarrierCondition.class);

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.CanDeleteCarrierCondition";

    /**
     * CDI Instance of {@link de.unibremen.swp2.kcb.service.CarrierService}
     */
    private CarrierService carrierService = getCarrierFacade();

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
        Carrier carrier = (Carrier) getAttributes().get("carrier");
        return carrierService.canDelete(carrier);
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
     * Manually retrieve injected instance of CarrierService
     * @return injected instance of CarrierService
     */
    @SuppressWarnings("unchecked")
    public CarrierService getCarrierFacade()
    {
        BeanManager bm = getBeanManager();
        Bean<CarrierService> bean = (Bean<CarrierService>) bm.getBeans(CarrierService.class).iterator().next();
        CreationalContext<CarrierService> ctx = bm.createCreationalContext(bean);
        return (CarrierService) bm.getReference(bean, CarrierService.class, ctx);
    }
}
