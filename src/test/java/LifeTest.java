import core.*;
import core.exceptions.AgentAlreadyDeadException;
import core.exceptions.InvalidPositionException;
import core.exceptions.LifeException;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class LifeTest {

    @Test
    public void testDefaultConstructor() throws LifeException {
        new Life();
    }

    @Test
    public void testSupportedAgentsCountIsCorrect() throws LifeException {
        final int deerCount = Utils.randomPositiveInteger(100);
        final int wolvesCount = Utils.randomPositiveInteger(100);
        final ArrayList<LifeAgentOptions> agentOptions = new ArrayList<>();

        LifeAgentOptions deerOptions = new LifeAgentOptions(Deer.class);
        LifeAgentOptions wolfOptions = new LifeAgentOptions(Wolf.class);
        deerOptions.setInitialCount(deerCount);
        wolfOptions.setInitialCount(wolvesCount);
        agentOptions.add(deerOptions);
        agentOptions.add(wolfOptions);
        LifeOptions options = new LifeOptions(agentOptions);

        Life life = new Life(options);
        assertEquals(deerCount + wolvesCount, life.getAgents().size());
    }

    /**
     *
     */
    @Test
    public void testLifeWithNullAgentParams() throws LifeException {
        ArrayList<LifeAgentOptions> agentParams = new ArrayList<>();

        LifeOptions options = new LifeOptions();
        Life life = new Life(options);
    }

    private double randDoubleOutOfRange() {
        Random rand = Utils.getRand();
        final int RANGE = rand.nextInt();
        double val;
        do {
            val = (-RANGE) + 2*RANGE*rand.nextDouble();
        } while(val < 1 && val > 0);
        return val;
    }

    @Test
    public void testMoveToAdjacentCell() {
        // TODO;
    }

    @Test
    public void testRun() throws LifeException {
        final int iterations = 30;
        Life life = new Life();

        for (int i = 0; i < iterations; i++) {
            try {
                life.step();
            } catch (InvalidPositionException e) {
                e.printStackTrace();
            } catch (AgentAlreadyDeadException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testAddAgent() {

    }

    // Not a unit-test
    // ===============
    @Test
    public void testNoMoreDeerWhenNoFoodAndNoReproduction() throws LifeException {
        final int nDeer = 1;
        final int eDeer = 10;

        // we don't want wolves but they have to be supported for step() to work
        LifeAgentOptions wolfOpts= new LifeAgentOptions(Wolf.class);
        wolfOpts.setInitialCount(0);

        // Deers
        LifeAgentOptions deerOpts = new LifeAgentOptions(Deer.class);
        deerOpts.setInitialCount(nDeer);
        deerOpts.setReproductionRate(0.0);
        deerOpts.setInitialEnergy(eDeer);
        LifeOptions options = new LifeOptions(deerOpts, wolfOpts);

        Life life = new Life(options);

        for (int i = 0; i < eDeer; i++) {
            assertEquals(1, life.getAgents().size());
            life.step();
        }
        assertEquals(0, life.getAgents().size());
    }
}