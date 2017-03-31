import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 30/03/2017.
 */
public class DeerTest {

    @Test
    public void tesDefaultDeer() throws AgentIsDeadException {
        new Deer();
    }

    @Test
    public void testDeerWithInitialEnergy() throws AgentIsDeadException {
        new Deer(new Random().nextInt(Integer.MAX_VALUE));
    }

    @Test
    public void testDeerIsAlive() throws AgentIsDeadException {
        Deer deer = new Deer();
        assertTrue(deer.isAlive());
    }

    @Test
    public void testDeerReproductionCreatesNewDeer() throws AgentIsDeadException {
        Deer deer = new Deer();
        LifeAgent agent = deer.reproduce();
        assertTrue(agent instanceof Deer);
    }

    @Test
    public void testDeerAgesCorrectly() throws AgentIsDeadException {
        final int initialEnergy = 100;
        final int ageBy = 10;
        Deer deer = new Deer(initialEnergy);
        deer.ageBy(ageBy);
        assertEquals(initialEnergy-ageBy, deer.getEnergy().intValue());
    }

//    @Test
//    public void testDeerMovesCorrectly() throws AgentIsDeadException {
//        Deer deer = new Deer();
//        Point2D p = Point2D.randomPoint(100);
//        deer.setPos(p);
//        assertEquals(p.getX(), deer.getPos().getX());
//        assertEquals(p.getY(), deer.getPos().getY());
//    }

}