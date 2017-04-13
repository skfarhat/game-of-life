package core;
import core.exceptions.AgentIsDeadException;

import java.util.List;

/**
 * @classs Wolf
 */
public class Wolf extends LifeAgent implements Consumes {

    /** @@brief default constructor */
    public Wolf() throws AgentIsDeadException {
        super();
    }

    public Wolf(int initialEnergy) throws AgentIsDeadException {
        super(initialEnergy);
    }

    public Wolf(Point2D p) throws AgentIsDeadException {
        super(p);
    }

    public Wolf(Point2D p, int energy) throws AgentIsDeadException {
        super(p, energy);
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        Wolf babyWolf = new Wolf(getPos(), MY_INITIAL_ENERGY);
        return babyWolf;
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

    @Override
    public String toString() {
        return String.format("Wolf[%s](e=%d)", getId().substring(0, 5), getEnergy());
    }
}
