/**
 * Created by Sami on 31/03/2017.
 */

package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @class Cell
 *
 * Cell in a grid with a position (Point2D) containing Agents
 */
public class Cell implements Positionable {

    /**
     * @brief the cell's position
     */
    private Point2D pos;

    /**
     * @brief list of agents that this cell contains
     */
    private List<Agent> agents = new ArrayList<>();

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

    private boolean hasGrass = false;

    /** @brief set new position */
    public void setPos(Point2D pos) { getPos().set(pos);  }

    /**
     * @brief add agent to the agents list
     * @return true if an agent was added, false if not. If the Cell already contains a Grass Agent,
     * not another will be added and false will be returned.
     * */
    public boolean addAgent(Agent a) {
        if (a instanceof Grass) {
            if (!hasGrass) {
                hasGrass = true;
                a.setPos(getPos());
                return agents.add(a);
            }
            else {
                return false;
            }
        }
        a.setPos(getPos());
        return agents.add(a);
    }

//    /**
//     * @brief remove agent from the agents lisst
//     * @param a agent to remove
//     * @return true if remove succeeded
//     */
//    public boolean removeAgent(Agent a) {
//        if (a instanceof Grass)
//            hasGrass = false;
//
//        return agents.remove(a);
//    }

    public boolean hasGrass() {
        return hasGrass;
    }

    /**
     * @return iterator over the agents that this cell contains
     */
    public Iterator<Agent> getAgents() {
        return agents.iterator();
    }

    /** @return number of agents contained in the cell */
    public int agentsCount() { return agents.size(); }

}
