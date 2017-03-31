/**
 * Created by Sami on 28/03/2017.
 */

/**
 * @class Agent
 *
 * Every agent in the system MUST have its own unique ID
 * Every agent in the system can reproduce
 */
public abstract class Agent {

    /** @brief agent's unique identifier string */
    private final String id;

    /** @brief agent's (x,y) position */
    private final Point2D pos;

    /** @brief default x position for a new agent */
    public static final int DEFAULT_X_POS = 0;

    /** @brief default y position for a new agent */
    public static final int DEFAULT_Y_POS = 0;

    /**
     * @brief default constructor
     * sets the id string using IdPool
     */
    public Agent() {
        id = IdPool.getInstance().newId();
        pos = new Point2D(DEFAULT_X_POS, DEFAULT_Y_POS); // default position for every new agent
    }

    /** @brief getter for ID */
    public String getId() {
        return id;
    }

    /** @brief getter for position */
    public Point2D getPos() {
        return pos;
    }
}
