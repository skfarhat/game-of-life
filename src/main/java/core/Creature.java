package core;

import core.exceptions.AgentAlreadyDeadException;
import core.exceptions.LifeImplementationException;
import core.interfaces.Moveable;
import java.lang.reflect.InvocationTargetException;

public class Creature extends LifeAgent implements Moveable {

    public Creature() throws AgentAlreadyDeadException {
    }

    public Creature(Point2D p) throws AgentAlreadyDeadException {
        super(p);
    }

    public Creature(int initialEnergy) throws AgentAlreadyDeadException {
        super(initialEnergy);
    }

    public Creature(Point2D p, Integer energy) throws AgentAlreadyDeadException {
        super(p, energy);
    }

    @Override
    public void moveTo(Point2D p) {
        setPos(p);
    }
}
