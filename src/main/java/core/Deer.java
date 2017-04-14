/**
 * Created by Sami on 30/03/2017.
 */

package core;

import core.exceptions.AgentIsDeadException;

import java.util.List;

public class Deer extends LifeAgent implements Consumes {

    /** @brief default constructor, calls LifeAgent's constructor */
    public Deer() throws AgentIsDeadException {
        super();
    }
    /**
     * @brief constructor for Deer with @param initial energy
     * @param initialEnergy of the deer
     */
    public Deer(int initialEnergy) throws AgentIsDeadException {
        super(initialEnergy);
    }

    public Deer(Point2D p) throws AgentIsDeadException {
        super(p);
    }

    public Deer(Point2D p, Integer energy) throws AgentIsDeadException {
        super(p, energy);
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        return new Deer(getPos(), this.MY_INITIAL_ENERGY);
    }

    @Override
    public String toString() {
        return String.format("Deer[%s](e=%d)", getId().substring(0, 5), getEnergy());
    }
}
