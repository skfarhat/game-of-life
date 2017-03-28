/**
 * Created by Sami on 28/03/2017.
 */

/**
 * Any agent in the system must have its own unique ID
 */
public class Agent {

    /** @brief agent's unique identifier string */
    private final String id;

    /**
     * @brief default constructor
     * sets the id string using IdPool
     */
    public Agent() {
        id = IdPool.getInstance().newId();
    }

    public String getId() {
        return id;
    }
}
