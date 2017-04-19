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
    public LifeAgent reproduce() throws AgentAlreadyDeadException {
        try {
            return getClass().getConstructor(Point2D.class, Integer.class).newInstance(getPos(), MY_INITIAL_ENERGY);
        } catch (InstantiationException e) {
            throw new LifeImplementationException(e.getMessage());
        } catch (IllegalAccessException e) {
            // prevented from (private) access
            throw new LifeImplementationException(e.getMessage());
        } catch (InvocationTargetException e) {
            // method  threw an exception
            throw new AgentAlreadyDeadException();
        } catch (NoSuchMethodException e) {
            throw new LifeImplementationException(e.getMessage());
        }
    }

    @Override
    public void moveTo(Point2D p) {
        setPos(p);
    }
}
