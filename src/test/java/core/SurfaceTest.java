package core;

import core.exceptions.AgentAlreadyDeadException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SurfaceTest {

    @Test
    public void testConstructor() throws AgentAlreadyDeadException {
        Surface s = new Surface() {
            @Override
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
                return null;
            }
        };
    }

    @Test
    public void testConstructorWithEnergy() throws AgentAlreadyDeadException {
        int e = Utils.randomIntegerInRange(10, 100);
        Surface s = new Surface(e) {
            @Override
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
                return null;
            }
        };
        assertEquals(e, s.getEnergy().intValue());
    }

    @Test
    public void testConstructorWithPosition() throws AgentAlreadyDeadException {
        Point2D p = Utils.randomPoint(100, 100);
        Surface s = new Surface(p) {
            @Override
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
                return null;
            }
        };
        assertEquals(p, s.getPos());
    }

    @Test
    public void testConstructorWithEnergyAndPos() throws AgentAlreadyDeadException {
        int e = Utils.randomPositiveInteger(100);
        Point2D p = Utils.randomPoint(100, 100);
        Surface s = new Surface(p, e) {
            @Override
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
                return null;
            }
        };
        assertEquals(p, s.getPos());
        assertEquals(e, s.getEnergy().intValue());

    }
}