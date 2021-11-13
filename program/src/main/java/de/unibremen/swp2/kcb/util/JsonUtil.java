package de.unibremen.swp2.kcb.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.unibremen.swp2.kcb.model.KCBEntity;

/**
 * The class of Json util.
 *
 * @author Marc
 * @author Marius
 */
public class JsonUtil {

    /**
     * Gson attribute
     */
    Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    /**
     * Marshal an object to a json string. This will also marshal all attributes of the object.
     *
     * @param obj the obj to be marshalled
     * @return the json representation of the object
     */
    public String marshal(final KCBEntity obj) {
        return obj != null ? obj.toJSON() : "null";
    }

    /**
     * Marshal an object to a json string.
     *
     * @param obj the obj to be marshalled
     * @return the json representation of the object
     */
    public String marshal(final Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Unmarshal json String to Object.
     *
     * @param s    the string to be processed
     * @param type the type to be processed
     * @return the represented object
     */
    public Object unmarshal(final String s, Class<KCBEntity> type) {
        return gson.fromJson(s, type);
    }
}
