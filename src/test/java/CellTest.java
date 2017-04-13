import core.*;
import core.exceptions.AgentIsDeadException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by Sami on 01/04/2017.
 */
public class CellTest {

    @Test
    public void getPosWithPointPassedInConstructor() throws Exception {
        Point2D p = Utils.randomPoint(30, 30);
        Cell cell = new Cell(p);
        assertEquals(cell.getPos(), p);
    }

    @Test
    public void getPosWithXandYPassedInConstructor() throws Exception {
        Point2D p = Utils.randomPoint(30, 30);
        Cell cell = new Cell(p.getX(), p.getY());
        assertEquals(cell.getPos(), p);
    }

//    @Test
//    public void setPos() throws Exception {
//        Point2D p1 = Utils.randomPoint(30, 30);
//        Point2D p2 = Utils.randomPoint(30, 30);
//        Cell cell = new Cell(p1);
//        cell.setPos(p2);
//
//        assertEquals(cell.getPos(), p2);
//    }

    @Test
    public void addAgent() throws Exception {
        Point2D p = Utils.randomPoint(30, 30);
        Cell cell = new Cell(p);
        Agent a = new LifeAgent() {
            @Override
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        cell.addAgent(a);

        // let's make sure we find the agent
        boolean found = false;
        for (Iterator<Agent> it = cell.getAgents(); it.hasNext() && !found;) {
            found = it.next() == a;
        }

        assertTrue(found);
    }

    @Test
    public void getAgents() throws Exception {
        Cell cell = new Cell(Utils.randomPoint(30, 30));

        // add random number of agents
        ArrayList<Agent> allAgents = new ArrayList<>();
        int N = Utils.randomPositiveInteger(30);
        for (int i = 0; i < N; i++) {
            Agent agent = new LifeAgent() {
                @Override
                public LifeAgent reproduce() throws AgentIsDeadException {
                    return null;
                }
            };
            cell.addAgent(agent);
            allAgents.add(agent);
        }

        for (Iterator<Agent> it = cell.getAgents(); it.hasNext();) {
            allAgents.contains(it.next());
        }
    }

    @Test
    public void testRemoveAgent() throws AgentIsDeadException {
        Cell cell = new Cell(Utils.randomPoint(30, 30));
        Agent a = new LifeAgent() {
            @Override
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        cell.addAgent(a);

        cell.removeAgent(a);
        assertFalse(cell.getAgents().hasNext());
    }

}