package core;

import core.exceptions.SurfaceAlreadyPresent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 */
public class LifeCell extends Cell<LifeAgent> {

    /**
     *  logger
     */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    private Surface surface;

    public LifeCell(Integer x, Integer y) {
        super(x, y);
    }

    public LifeCell(Point2D pos1) {
        super(pos1);
    }

    /**
     * @return list of core.agents that were removed
     */
    public List<LifeAgent> removeDeadAgents() {

        List<LifeAgent> toRemove = findDeadAgents();
        agents.removeAll(toRemove);
        return toRemove;
    }

    /** @return list of dead core.agents in this cell */
    public List<LifeAgent> findDeadAgents() {
        return agents.stream().filter(a -> !a.isAlive()).collect(Collectors.toList());
    }


    /**
     * add creature to the core.agents list
     * @return true if adding the creature is successful, false otherwise. If the Cell already contains a Grass Agent,
     * no other will be added and false will be returned.
     * */
    public boolean addAgent(Creature c) {

        // All moveTo() does in the current implementation is to change the position of the Creature. This is already done
        // in Cell.addAgent() so the call to c.moveTo() may be thought to be redundant. Logically speaking it is still needed,
        // because Cell will always want to set the position of the agent it adds. And when moveTo()'s implementation fails
        // to do so (wrong implementation) we still know that Agent.addAgent() will do the job.
        c.moveTo(getPos());
        return super.addAgent(c);
    }

    /**
     * add agent to the core.agents list
     * @return true if an agent was added, false if not. If the Cell already contains a Grass Agent,
     * no other will be added and false will be returned.
     * */
    public boolean addAgent(Surface s) throws SurfaceAlreadyPresent {
        // if the LifeAgent is an instance of surface and the Cell already has a Surface
        if (this.surface != null) {
            LOGGER.log(Level.FINEST, "addAgent() failed: cannot add another Surface to cell");
            throw new SurfaceAlreadyPresent();
        }
        else {
            this.surface = s;
            this.surface.setPos(getPos());
            return super.addAgent(this.surface);
        }
    }

    public boolean removeAgent(Creature c) {
        boolean check = super.removeAgent(c);
        if (check)
            this.surface = null;
        return check;
    }

    private boolean removeAgent(Surface s) {
        boolean check = super.removeAgent(s);
        if (check)
            this.surface = null;
        return check;
    }

    public boolean containsSurface() { return surface != null; }

    public Surface getSurface() { return surface; }
}
