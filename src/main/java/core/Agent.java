/**
 * Created by Sami on 28/03/2017.
 */

package core;

import core.interfaces.Identifiable;
import core.interfaces.Positionable;

/**
 *  Agent
 *
 * Every agent in the system MUST have its own unique ID
 * Every agent in the system can reproduce
 */
public abstract class Agent implements Positionable, Identifiable {

    /**  agent's unique identifier string */
    private final String id;

    /**  agent's (x,y) position */
    private Point2D pos;

    /**  default x position for a new agent */
    public static final int DEFAULT_X_POS = 0;

    /**  default y position for a new agent */
    public static final int DEFAULT_Y_POS = 0;

    /**
     *  default constructor
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

    /**  getter for ID */
    @Override
    public String getId() {
        return id;
    }

    /**  getter for position */
    @Override
    public Point2D getPos() {
        return pos;
    }

    /**  getter for position */
    public void setPos(Point2D pos) {
        this.pos = new Point2D(pos);
    }

}
