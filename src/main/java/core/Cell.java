package core;

import core.exceptions.SurfaceAlreadyPresent;
import core.interfaces.Positionable;

import java.util.*;

/**
 * Cell
 *
 * Cell in a grid with a position (Point2D) containing Agents
 */
public class Cell<T extends Agent> implements Positionable {

    /**
     *  the cell's position
     */
    private Point2D pos;

    /**
     *  list of agents that this cell contains
     */
    protected List<T> agents = new Vector<>();

    /**
     * @param x coordinate of this cell's position
     * @param y coordinate of this cell's position
     *  constructor with x,y passed instead of Point2D
     */
    public Cell(Integer x, Integer y) {
        this(new Point2D(x, y));
    }

    /**
     *  constructor with Point2D position
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

    /**
     * add agent to the agents list
     * @return true if an agent was added, false if not. If the Cell already contains a Grass Agent,
     * no other will be added and false will be returned.
     * */
    public boolean addAgent(T t) {
        t.setPos(getPos());
        return agents.add(t);
    }

    /**
     * remove agent from the agents lisst
     * @param t agent to remove
     * @return true if remove succeeded
     */
    public boolean removeAgent(T t) {
        return agents.remove(t);
    }

    public List<T> getAgentsCopy() {
        ArrayList<T> dst = new ArrayList<>(agentsCount());
        dst.addAll(agents);
        return dst;
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
