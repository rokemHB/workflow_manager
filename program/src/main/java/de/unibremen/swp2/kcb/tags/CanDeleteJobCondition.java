package de.unibremen.swp2.kcb.tags;

import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.service.JobService;
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
 * Class CanDeleteJobCondition
 *
 * @author Arvid
 */
@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
        tagName = "canDeleteJob", value = "de.unibremen.swp2.kcb.tags.CanDeleteJobCondition")
public class CanDeleteJobCondition extends UIComponentBase {

    /**
     * Logger object of the CanDeleteJobCondition class
     */
    private static final Logger logger = LogManager.getLogger(CanDeleteJobCondition.class);

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.CanDeleteJobCondition";

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
     */
    @Override
    public boolean isRendered() {
        Job job = (Job) getAttributes().get("job");
        return job.getAssemblies() == null || job.getAssemblies().isEmpty();
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
     * Manually retrieve injected instance of JobService
     * @return injected instance of JobService
     */
    @SuppressWarnings("unchecked")
    public JobService getJobFacade()
    {
        BeanManager bm = getBeanManager();
        Bean<JobService> bean = (Bean<JobService>) bm.getBeans(JobService.class).iterator().next();
        CreationalContext<JobService> ctx = bm.createCreationalContext(bean);
        return (JobService) bm.getReference(bean, JobService.class, ctx);
    }
}
