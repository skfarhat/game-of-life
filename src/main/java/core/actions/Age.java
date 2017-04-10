package core.actions;

import core.LifeAgent;

public class Age extends Action<LifeAgent> {


    private int ageBy;

    public Age(LifeAgent lifeAgent, int ageBy) {
        super(lifeAgent);
        this.ageBy = ageBy;
    }

    public int getAgeBy() {
        return ageBy;
    }
}
