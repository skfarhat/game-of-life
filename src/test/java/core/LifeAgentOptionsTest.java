package core;

import core.exceptions.AgentAlreadyDeadException;
import core.exceptions.LifeImplementationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class LifeAgentOptionsTest {

    /**
     * subclass of LifeAgent that doesn't implement getDefaults()
     */
    class Lion extends LifeAgent {
        public Lion() throws AgentAlreadyDeadException {
        }

        public Lion(Point2D p) throws AgentAlreadyDeadException {
            super(p);
        }

        public Lion(int initialEnergy) throws AgentAlreadyDeadException {
            super(initialEnergy);
        }

        public Lion(Point2D p, Integer energy) throws AgentAlreadyDeadException {
            super(p, energy);
        }

        @Override
        public LifeAgent reproduce() throws AgentAlreadyDeadException {
            return null;
        }
    }

    @Test
    public void testCopyConstructor() throws LifeImplementationException {
        LifeAgentOptions lifeAgentOptions = new LifeAgentOptions(Grass.class);

        final Integer age = Utils.randomPositiveInteger(100);
        final Integer energyGain = Utils.randomPositiveInteger(100);
        final Integer energyLoss = Utils.randomPositiveInteger(100);
        final Double reproductionRate = Utils.getRand().nextDouble();
        final Integer initialCount = Utils.randomPositiveInteger(100);
        final Integer initialEnergy = Utils.randomPositiveInteger(100);

        lifeAgentOptions.setEnergyGained(energyGain);
        lifeAgentOptions.setEnergyLost(energyLoss);
        lifeAgentOptions.setInitialCount(initialCount);
        lifeAgentOptions.setInitialEnergy(initialEnergy);
        lifeAgentOptions.setReproductionRate(reproductionRate);
        lifeAgentOptions.setAgeBy(age);

        LifeAgentOptions copyOfOpts = new LifeAgentOptions(lifeAgentOptions);

        assertEquals(age, copyOfOpts.getAgeBy());
        assertEquals(energyGain, copyOfOpts.getEnergyGained());
        assertEquals(energyLoss, copyOfOpts.getEnergyLost());
        assertEquals(initialCount, copyOfOpts.getInitialCount());
        assertEquals(initialEnergy, copyOfOpts.getInitialEnergy());
    }

    @Test
    public void testConstructorWithDefaultsChoosesDefaultsFromSubclass() throws LifeImplementationException {
        LifeAgentOptions lifeAgentOptions = new LifeAgentOptions(Grass.class);

        assertEquals(Grass.class, lifeAgentOptions.getAgentType());
        assertEquals(Grass.DEFAULT_AGE, lifeAgentOptions.getAgeBy());
        assertEquals(Grass.DEFAULT_E0, lifeAgentOptions.getInitialEnergy());
        assertEquals(Grass.DEFAULT_I0, lifeAgentOptions.getInitialCount());
        assertEquals(Grass.DEFAULT_E_GAIN, lifeAgentOptions.getEnergyGained());
        assertEquals(Grass.DEFAULT_E_LOSS, lifeAgentOptions.getEnergyLost());
        assertEquals(Grass.DEFAULT_R, lifeAgentOptions.getReproductionRate());
    }

    @Test
    public void testConstructorWithDefaultsChoosesLifeAgentOptionDefaultsWhenSubclassDoesNotHaveDefaults() throws LifeImplementationException, AgentAlreadyDeadException {
        LifeAgent agent = new Lion();

        Class<?extends LifeAgent> agentClass = agent.getClass();
        LifeAgentOptions lifeAgentOptions = new LifeAgentOptions(agentClass);

        assertEquals(Lion.class, lifeAgentOptions.getAgentType());
        assertEquals(LifeAgentOptions.DEFAULT_AGE, lifeAgentOptions.getAgeBy());
        assertEquals(LifeAgentOptions.DEFAULT_E0, lifeAgentOptions.getInitialEnergy());
        assertEquals(LifeAgentOptions.DEFAULT_I0, lifeAgentOptions.getInitialCount());
        assertEquals(LifeAgentOptions.DEFAULT_E_GAIN, lifeAgentOptions.getEnergyGained());
        assertEquals(LifeAgentOptions.DEFAULT_E_LOSS, lifeAgentOptions.getEnergyLost());
        assertEquals(LifeAgentOptions.DEFAULT_R, lifeAgentOptions.getReproductionRate());
    }

    @Test
    public void testConstructorWithParams() {
        final int bound = 1000;
        Integer ageBy = Utils.randomPositiveInteger(bound);
        Integer energyGain = Utils.randomPositiveInteger(bound);
        Integer energyLoss = Utils.randomPositiveInteger(bound);
        Integer initialCount = Utils.randomPositiveInteger(bound);
        Integer initialEnergy = Utils.randomPositiveInteger(bound);
        Double reproductionRate = Utils.getRand().nextDouble();

        LifeAgentOptions params = new LifeAgentOptions(Wolf.class, ageBy, initialEnergy, reproductionRate,
                initialCount, energyGain, energyLoss);

        assertEquals(Wolf.class, params.getAgentType());
        assertEquals(ageBy, params.getAgeBy());
        assertEquals(initialEnergy, params.getInitialEnergy());
        assertEquals(initialCount, params.getInitialCount());
        assertEquals(energyGain, params.getEnergyGained());
        assertEquals(energyLoss, params.getEnergyLost());
        assertEquals(reproductionRate, params.getReproductionRate());
    }
}