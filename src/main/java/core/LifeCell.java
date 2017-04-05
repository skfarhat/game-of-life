package core;

import java.util.List;
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
    public List<LifeAgent> recycleDeadAgents() {
        List<LifeAgent> toRemove = agents.stream().filter(a -> !((LifeAgent)a).isAlive()).collect(Collectors.toList());
        agents.removeAll(toRemove);
        return toRemove;
    }

    public boolean addAgent(LifeAgent a) {
        if (a instanceof Grass) {
            if (!containsGrass) {
                containsGrass = true;
                a.setPos(getPos());
                return agents.add(a);
            }
            else {
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
