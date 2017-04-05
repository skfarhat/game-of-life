package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @class Cell
 *
 * Cell in a grid with a position (Point2D) containing Agents
 */
public class Cell<T extends Agent> implements Positionable {

    /**
     * @brief the cell's position
     */
    private Point2D pos;

    /**
     * @brief list of agents that this cell contains
     */
    protected List<T> agents = new ArrayList<>();

    /**
     * @param x coordinate of this cell's position
     * @param y coordinate of this cell's position
     * @brief constructor with x,y passed instead of Point2D
     */
    public Cell(Integer x, Integer y) {
        this(new Point2D(x, y));
    }

    /**
     * @brief constructor with Point2D position
     */
    public Cell(Point2D pos1) {
        pos = new Point2D(pos1.getX(), pos1.getY());
    }

    /**
     * @return this cell's position
     */
    public Point2D getPos() {
        return pos;
    }

    /** @brief set new position */
    public void setPos(Point2D pos) { getPos().set(pos);  }

    /**
     * @brief add agent to the agents list
     * @return true if an agent was added, false if not. If the Cell already contains a Grass Agent,
     * not another will be added and false will be returned.
     * */
    public boolean addAgent(T t) {
        t.setPos(getPos());
        return agents.add(t);
    }

    /**
     * @brief remove agent from the agents lisst
     * @param t agent to remove
     * @return true if remove succeeded
     */
    public boolean removeAgent(T t) {
        synchronized (agents) {
            return agents.remove(t);
        }
    }

    /**
     * @return iterator over the agents that this cell contains
     */
    public Iterator<T> getAgents() {
        return agents.iterator();
    }

    /** @return number of agents contained in the cell */
    public int agentsCount() { return agents.size(); }

}
