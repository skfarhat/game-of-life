/**
 * Created by Sami on 28/03/2017.
 */

/**
 * @class Agent
 *
 * Every agent in the system MUST have its own unique ID
 * Every agent in the system can reproduce
 *
 */
public abstract class Agent {

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
