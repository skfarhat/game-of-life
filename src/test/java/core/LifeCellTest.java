package core;

import core.exceptions.AgentIsDeadException;
import core.exceptions.GridCreationException;
import core.exceptions.InvalidPositionException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Sami on 05/04/2017.
 */
public class LifeCellTest {

    @Test
    public void testRemoveDeadAgentsFromCell() throws AgentIsDeadException, GridCreationException, InvalidPositionException {

        final int nAgents = 10 + Utils.randomPositiveInteger(91); // number of agents to create
        final int nKill = 1 + Utils.randomPositiveInteger(nAgents-1); // number of agents to kill
        ArrayList<LifeAgent> allAgents = new ArrayList<>(nAgents); // list of created agents
        ArrayList<LifeAgent> agentsKilled = new ArrayList<>(nKill); // list of killed agents
        ArrayList<LifeAgent> agentsAlive = new ArrayList<>(nAgents - nKill); // list of all agents that are still alive

        LifeCell cell = new LifeCell(new Point2D(0, 0));

        // create agents
        for (int i = 0; i < nAgents; i++) {
            LifeAgent agent = new LifeAgent() {
                @Override
                public LifeAgent reproduce() throws AgentIsDeadException {
                    return null;
                }
            };
            allAgents.add(agent);
            agentsAlive.add(agent);
            cell.addAgent(agent);
        }

        // kill some agents
        for (int i = 0; i < nKill; i++) {

            // find an agent that hasn't been killed yet
            int index = Utils.randomPositiveInteger(agentsAlive.size());
            LifeAgent agentToKill = agentsAlive.get(index);

            // kill them
            agentToKill.die();

            // add and remove them from the concerned lists
            assertTrue(agentsAlive.remove(agentToKill));
            assertTrue(agentsKilled.add(agentToKill));
        }

        List<LifeAgent> returnedList = cell.removeDeadAgents();

        assertEquals("size of the returned list should be equal to the number of killed agents",
                nKill, returnedList.size());
        // all agents in returnedList should be present in the agentsKilled List
        for (LifeAgent agent: returnedList) {
            assertFalse(agent.isAlive()); // check the agents is dead
            assertTrue(agentsKilled.contains(agent));
        }
    }


    @Test
    public void addAgent() throws Exception {

    }

    @Test
    public void removeAgent() throws Exception {

    }

    @Test
    public void isContainsGrass() throws Exception {

    }

}