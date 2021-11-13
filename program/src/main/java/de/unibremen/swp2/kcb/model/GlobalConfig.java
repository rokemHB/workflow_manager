package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.*;

/**
 * Model that describes a config object.
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
@NamedQueries({
  @NamedQuery(name = "findGlobalConfigsByName", query = "SELECT v FROM GlobalConfig v WHERE v.key = :key")
})
public class GlobalConfig extends KCBEntity {
    /**
     * the key of the config setting
     */
    @Id
    @Expose
    private String key;

    /**
     * The value of the config setting
     */
    @Column
    @Expose
    private String value;
}
