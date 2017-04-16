package core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sami on 16/04/2017.
 */
public class LifeAgentParamsTest {

    @Test
    public void testConstructorWithDefaults() {
        LifeAgentParams params = new LifeAgentParams(Wolf.class);
        assertEquals(Wolf.class, params.getAgentType());
        assertEquals(LifeAgentParams.DEFAULT_AGE, params.getAgeBy());
        assertEquals(LifeAgentParams.DEFAULT_E0, params.getInitialEnergy());
        assertEquals(LifeAgentParams.DEFAULT_I0, params.getInitialCount());
        assertEquals(LifeAgentParams.DEFAULT_E_GAIN, params.getEnergyGained());
        assertEquals(LifeAgentParams.DEFAULT_E_LOSS, params.getEnergyLost());
        assertEquals(LifeAgentParams.DEFAULT_R, params.getReproductionRate());
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

        LifeAgentParams params = new LifeAgentParams(Wolf.class, ageBy, initialEnergy, reproductionRate,
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