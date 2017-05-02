package agents;

import core.LifeAgent;
import core.Point2D;
import core.Utils;
import core.exceptions.AgentAlreadyDeadException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

public class GrassTest {

    @Test
    public void testDefaultGrass() throws AgentAlreadyDeadException {
        new Grass();
    }

    @Test
    public void testGrassWithInitialEnergy() throws AgentAlreadyDeadException {
        final int INITIAL_ENERGY = new Random().nextInt(Integer.MAX_VALUE);
        new Grass(INITIAL_ENERGY);
    }

    @Test
    public void testConstructorWithPos() throws AgentAlreadyDeadException {
        Point2D p = Utils.randomPoint(100, 100);
        Grass g = new Grass(p);
        assertEquals(p, g.getPos());
    }

    @Test
    public void testConstructorWithPosAndEnergy() throws AgentAlreadyDeadException {
        final Point2D p = Utils.randomPoint(100, 100);
        final int e = new Random().nextInt(Integer.MAX_VALUE);
        Grass g = new Grass(p, e);
        assertEquals(p, g.getPos());
        assertEquals(e, g.getEnergy().intValue());
    }

    @Test
    public void testGrassReproductionGivesNewGrass() throws AgentAlreadyDeadException {
        Grass grass = new Grass();
        LifeAgent agent = grass.reproduce();
        assertTrue(agent instanceof Grass);
    }

    @Test
    public void testToString() throws AgentAlreadyDeadException {
        Grass grass = new Grass();
        String str = grass.toString();
        // make sure the to String contains the word grass
        assertTrue(str.toLowerCase().indexOf("grass") > -1);
    }


}