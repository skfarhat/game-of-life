/**
 * Created by Sami on 28/03/2017.
 */
package core;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

public class IdPool {

    /** @brief reference to singleton IdPool, kept private of course */
    private final static  IdPool pool = new IdPool();

    /** @brief used to generate the random id strings */
    private final static SecureRandom random = new SecureRandom();

    /** @brief map used to store the generated ids to avoid creating duplicates */
    HashMap<String, Boolean> ids = new HashMap<String, Boolean>();

    /** @brief private constructor */
    private IdPool() { }

    /** @brief returns singleton instance of IdPool */
    public static IdPool getInstance() {
        return pool;
    }

    /**
     * @brief generate a new id
     * @return geneerated id
     * */
    public String newId() {
        String generated = null;
        do {
            generated = Utils.randomString();
        } while (idExists(generated));

        ids.put(generated, true);
        return generated;
    }

    /**
     *
     * @param id for which we want to check the existence
     * @return true if the passed id exists
     */
    public boolean idExists(String id) {
        return ids.containsKey(id);
    }
}
