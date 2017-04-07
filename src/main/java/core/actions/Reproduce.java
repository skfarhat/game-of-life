package core.actions;

import core.LifeAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sami on 07/04/2017.
 */
public class Reproduce extends Action<LifeAgent> {

    List<LifeAgent> babies;

    public Reproduce(LifeAgent lifeAgent, List<LifeAgent> babies) {
        super(lifeAgent);
        this.babies = babies;
    }
    public Reproduce(LifeAgent lifeAgent, LifeAgent baby) {
        super(lifeAgent);
        this.babies = new ArrayList<>();
        this.babies.add(baby);
    }
}
