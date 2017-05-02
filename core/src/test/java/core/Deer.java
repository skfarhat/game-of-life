package core;

import core.exceptions.AgentAlreadyDeadException;

public class Deer extends Creature {
    public Deer() throws AgentAlreadyDeadException {
    }

    public Deer(Point2D p) throws AgentAlreadyDeadException {
        super(p);
    }

    public Deer(int initialEnergy) throws AgentAlreadyDeadException {
        super(initialEnergy);
    }

    public Deer(Point2D p, Integer energy) throws AgentAlreadyDeadException {
        super(p, energy);
    }
}
