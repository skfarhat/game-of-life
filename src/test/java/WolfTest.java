import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sami on 28/03/2017.
 */
public class WolfTest {

    @Test
    public void testWolfIsAlive() throws AgentIsDeadException {
        Wolf wolf = new Wolf();
        assertTrue(wolf.isAlive());
    }

    @Test
    public void testWolfHasCorrectInitialEnergy() throws AgentIsDeadException {
        final int initialEnergy = 100;
        Wolf wolf = new Wolf(initialEnergy);
        assertEquals(initialEnergy, wolf.getEnergy().intValue());
    }

    @Test
    public void testWolfReproductionCreatesNewWolf() throws AgentIsDeadException {
        Wolf wolf = new Wolf();
        LifeAgent agent = wolf.reproduce();
        assertTrue(agent instanceof Wolf);
    }

    @Test
    public void testWolfAgesCorrectly() throws AgentIsDeadException {
        final int initialEnergy = 100;
        final int ageBy = 10;
        Wolf wolf = new Wolf(initialEnergy);
        wolf.ageBy(ageBy);
        assertEquals(initialEnergy-ageBy, wolf.getEnergy().intValue());
    }

}