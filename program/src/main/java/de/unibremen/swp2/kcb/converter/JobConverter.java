package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.service.JobService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type job converter.
 *
 * @author Arvid
 */
@FacesConverter(forClass = Job.class)
public class JobConverter implements Converter<Job> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private JobService jobService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public Job getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return jobService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param job the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Job job) {
        return job.getId();
    }
}
