package core;

import java.util.ArrayList;
import java.util.Iterator;
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

        ArrayList<LifeAgent> toRemove = new ArrayList<>();
        Iterator<LifeAgent> it = agents.iterator();
        while(it.hasNext()) {
            LifeAgent agent = it.next();
            if (!agent.isAlive()) {
                toRemove.add(agent);
                it.remove();
            }
        }
        return toRemove;
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
