package core;

import core.exceptions.AgentIsDeadException;

public abstract class Surface extends LifeAgent {

    public Surface() throws AgentIsDeadException {
    }

    public Surface(Point2D p) throws AgentIsDeadException {
        super(p);
    }

    public Surface(int initialEnergy) throws AgentIsDeadException {
        super(initialEnergy);
    }

    public Surface(Point2D p, Integer energy) throws AgentIsDeadException {
        super(p, energy);
    }
}
