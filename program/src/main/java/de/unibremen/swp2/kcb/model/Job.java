package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


/**
 * Executes a ProcessChain
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class Job extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the name of the job
     */
    @Column (unique = true)
    @Expose
    private String name;

    /**
     * the state of the job
     */
    @Column
    @Expose
    private JobState jobState = JobState.PENDING;

    /**
     * the ProcessChain the Job executes
     *
     * @see ProcessStep
     */
    @ManyToOne
    @Expose
    public ProcessChain processChain;

    /**
     * the Priority of the Job
     */
    @ManyToOne
    @Expose
    private Priority priority;

    /**
     * Contains all Assemblies for the Job to execute
     */
    @ManyToMany
    @Expose
    private List<Assembly> assemblies;

    /**
     * Contains all Procedures for the Job
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @Expose
    private List<Procedure> procedures;

    /**
     * Equals method of Job class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Job)) return false;
        return this.getId().equals(((Job) o).getId());
    }

    /**
     * Checks whether other Object is an instance of Job
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Job;
    }

    /**
     * HashCode method of Job
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $jobState = this.getJobState();
        result = result * PRIME + ($jobState == null ? 43 : $jobState.hashCode());
        final Object $processChain = this.getProcessChain();
        result = result * PRIME + ($processChain == null ? 43 : $processChain.hashCode());
        final Object $priority = this.getPriority();
        result = result * PRIME + ($priority == null ? 43 : $priority.hashCode());
        final Object $assemblies = this.getAssemblies();
        result = result * PRIME + ($assemblies == null ? 43 : $assemblies.hashCode());
        final Object $procedures = this.getProcedures();
        result = result * PRIME + ($procedures == null ? 43 : $procedures.hashCode());
        return result;
    }

    /**
     * Gets assemblies of the model class.
     *
     * @return the assemblies
     */
    public List<Assembly> getAssemblies() {
        return this.assemblies;
    }
}
