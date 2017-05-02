package core.actions;

import core.LifeAgent;

public class EnergyChange extends Action<LifeAgent> {

    private int energyDelta; // negative if aging, positive if gaining

    public EnergyChange(LifeAgent lifeAgent, int energyDelta) {
        super(lifeAgent);
        this.energyDelta = energyDelta;
    }

    public int getEnergyDelta() {
        return energyDelta;
    }

    @Override
    public String toString() {
        return String.format("[EnergyChange(%s) = %d]", getAgent(), energyDelta);
    }
}
