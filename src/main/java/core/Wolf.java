package core;
import core.exceptions.AgentIsDeadException;

import java.util.List;

/**
 * @classs Wolf
 */
public class Wolf extends LifeAgent {

    /**
     * @@brief default constructor
     */
    public Wolf() throws AgentIsDeadException {
        super();
    }

    public Wolf(int initialEnergy) throws AgentIsDeadException {
        super(initialEnergy);
    }

    public Wolf(Point2D p) throws AgentIsDeadException {
        super(p);
    }

    public Wolf(Point2D p, Integer energy) throws AgentIsDeadException {
        super(p, energy);
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        Wolf babyWolf = new Wolf(getPos(), MY_INITIAL_ENERGY);
        return babyWolf;
    }

    @Override
    public String toString() {
        return String.format("Wolf[%s]%s(e=%d)", getId().substring(0, 5), getPos(), getEnergy());
    }
}

