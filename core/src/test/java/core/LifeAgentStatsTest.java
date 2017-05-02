package core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LifeAgentStatsTest {

    @Test
    public void incNbDied() throws Exception {
        LifeAgentStats s = new LifeAgentStats(Wolf.class);
        assertEquals(0, s.getNbDied()); // initially zero

        int rand = Utils.randomIntegerInRange(10, 100);
        for (int i = 0; i < rand; i++)
            s.incNbDied();

        assertEquals(rand, s.getNbDied());
    }

    @Test
    public void incNbCreated() throws Exception {
        LifeAgentStats s = new LifeAgentStats(Wolf.class);
        assertEquals(0, s.getNbCreated()); // initially zero

        int rand = Utils.randomIntegerInRange(10, 100);
        for (int i = 0; i < rand; i++)
            s.incNbCreated();

        assertEquals(rand, s.getNbCreated());
    }

    @Test
    public void incNbReproduced() throws Exception {
        LifeAgentStats s = new LifeAgentStats(Wolf.class);
        assertEquals(0, s.getNbReproduced()); // initially zero

        int rand = Utils.randomIntegerInRange(10, 100);
        for (int i = 0; i < rand; i++)
            s.incNbReproduced();

        assertEquals(rand, s.getNbReproduced());
    }

    @Test
    public void getAgentType() throws Exception {
        LifeAgentStats s = new LifeAgentStats(Wolf.class);
        assertEquals(Wolf.class, s.getAgentType());
    }
}