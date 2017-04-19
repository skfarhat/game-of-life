package core;

import core.exceptions.AgentAlreadyDeadException;

public abstract class Surface extends LifeAgent {

    public Surface() throws AgentAlreadyDeadException {
    }

    public Surface(Point2D p) throws AgentAlreadyDeadException {
        super(p);
    }

    public Surface(int initialEnergy) throws AgentAlreadyDeadException {
        super(initialEnergy);
    }

    public Surface(Point2D p, Integer energy) throws AgentAlreadyDeadException {
        super(p, energy);
    }
}
