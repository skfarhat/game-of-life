package core.actions;

import core.LifeAgent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public Iterator<LifeAgent> getBabies() {
        return babies.iterator();
    }

    @Override
    public String toString() {
        return String.format("[Reproduce(%s): %s]", getAgent(), babies.toString());
    }
}
