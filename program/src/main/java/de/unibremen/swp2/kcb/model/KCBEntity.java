package de.unibremen.swp2.kcb.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import javax.persistence.Version;
import java.io.Serializable;

/**
 * Interface to be implemented by all Entities in the Workflow Manager
 *
 * @author Marius
 */
@Data
public abstract class KCBEntity implements Serializable {
    private Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    @Version
    private int version;

    /**
     * Converts an Entity to it's JSON representation.
     *
     * @return JSON representation of this object
     */
    public String toJSON() {
        return gson.toJson(this);
    }
}
