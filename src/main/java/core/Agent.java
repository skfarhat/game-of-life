/**
 * Created by Sami on 28/03/2017.
 */

package core;

/**
 * @class Agent
 *
 * Every agent in the system MUST have its own unique ID
 * Every agent in the system can reproduce
 */
public abstract class Agent implements Positionable {

    /** @brief agent's unique identifier string */
    private final String id;

    /** @brief agent's (x,y) position */
    private Point2D pos;

    /** @brief default x position for a new agent */
    public static final int DEFAULT_X_POS = 0;

    /** @brief default y position for a new agent */
    public static final int DEFAULT_Y_POS = 0;

    /**
     * @brief default constructor
     * sets the id string using IdPool
     */
    public Agent() {
        this(new Point2D(DEFAULT_X_POS, DEFAULT_Y_POS));
    }

    /**
     * @param pos
     */
    public Agent(Point2D pos) {
        id = IdPool.getInstance().newId();
        // TODO(sami): check on the position - throw exception
        if (pos == null)
            this.pos = new Point2D(0, 0);
        else
            this.pos = pos;
    }

    /** @brief getter for ID */
    public String getId() {
        return id;
    }

    /** @brief getter for position */
    public Point2D getPos() {
        return pos;
    }

    /** @brief getter for position */
    public void setPos(Point2D pos) {
        this.pos = new Point2D(pos);
    }

}
