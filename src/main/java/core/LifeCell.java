package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @class
 */
public class LifeCell extends Cell<LifeAgent> {

    private boolean containsGrass = false;

    public LifeCell(Integer x, Integer y) {
        super(x, y);
    }

    public LifeCell(Point2D pos1) {
        super(pos1);
    }

    /**
     * @return list of agents that were removed
     */
    public List<LifeAgent> removeDeadAgents() {

        List<LifeAgent> toRemove = findDeadAgents();
        agents.removeAll(toRemove);
        return toRemove;
    }

    /** @return list of dead agents in this cell */
    public List<LifeAgent> findDeadAgents() {
        return agents.stream().filter(a -> !a.isAlive()).collect(Collectors.toList());
    }

    public boolean addAgent(LifeAgent a) {
        if (a instanceof Grass) {
            if (!containsGrass) {
                containsGrass = true;
            }
            else {
                // TODO(sami): remove
                System.out.println("FYI: addAgent returning false");
                // we're trying to add grass to a cell but it is already there..
                return false;
            }
        }
        return super.addAgent(a);
    }

    public boolean removeAgent(LifeAgent a) {
        if (a instanceof Grass)
            containsGrass = false;

        return super.removeAgent(a);
    }

    /** @return whether the cell contains grass */
    public boolean isContainsGrass() {
        return containsGrass;
    }
}
