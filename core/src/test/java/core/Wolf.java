package core;

import core.exceptions.AgentAlreadyDeadException;

public class Wolf extends Creature {
    public Wolf() throws AgentAlreadyDeadException {
    }

    public Wolf(Point2D p) throws AgentAlreadyDeadException {
        super(p);
    }

    public Wolf(int initialEnergy) throws AgentAlreadyDeadException {
        super(initialEnergy);
    }

    public Wolf(Point2D p, Integer energy) throws AgentAlreadyDeadException {
        super(p, energy);
    }
}
