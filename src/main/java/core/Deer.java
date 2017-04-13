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

    public Deer(Point2D p, int energy) throws AgentIsDeadException {
        super(p, energy);
    }

    // TODO(sami): replace with a new exception
    @Override
    public boolean consume(Consumable consumable) throws AgentIsDeadException {
        if (consumable == this)
            throw new IllegalArgumentException("Cannot consume myself!");

        try {
            consumable.die();
            return true;
        }
        catch(AgentIsDeadException exc) {
            return false;
        }
    }

    @Override
    public int consumeAll(List<Consumable> consumables) throws AgentIsDeadException {
        int count = 0;
        for (Consumable consumable : consumables) {
            // if consume succeeds increment count
            if (consume(consumable))
                count++;
        }
        return count;
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        return new Deer(getPos(), this.MY_INITIAL_ENERGY);
    }

    @Override
    public String toString() {
        return String.format("Deer[%s](e=%d)", getId().substring(0, 5), getEnergy());
    }
}
