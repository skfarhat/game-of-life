package core;

import core.exceptions.LifeException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LifeOptionsTest {

    @Test
    public void testDefaultConstructor() throws Exception {
        new LifeOptions();
    }

    @Test
    public void testSupportedAgentsEmptyForDefaultLifeOptions() throws Exception {
        LifeOptions options = new LifeOptions();
        assertEquals(0,  options.getSupportedAgents().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListOfSupportedAgentsIsImmutable() {
        ArrayList<LifeAgentParams> params = new ArrayList<>();
        params.add(new LifeAgentParams(Wolf.class));
        LifeOptions options = new LifeOptions(params);
        options.getSupportedAgents().remove(0);
    }

    @Test
    public void testDefaultMaxIterations() {
        LifeOptions options = new LifeOptions();
        assertEquals(LifeOptions.DEFAULT_MAX_ITERATIONS,  options.getMaximumIterations());
    }

    @Test
    public void testDefaultColumns() {
        LifeOptions options = new LifeOptions();
        assertEquals(LifeOptions.DEFAULT_GRID_N,  options.getGridCols());
    }

    @Test
    public void testDefaultRows() {
        LifeOptions options = new LifeOptions();
        assertEquals(LifeOptions.DEFAULT_GRID_N,  options.getGridRows());
    }

    @Test
    public void testGetOptionsForAgent() {
        ArrayList<LifeAgentParams> params = new ArrayList<>();
        LifeAgentParams lifeAgentParams = new LifeAgentParams(Deer.class);
        params.add(lifeAgentParams);
        LifeOptions options = new LifeOptions(params);

        assertEquals(lifeAgentParams, options.getOptionsForAgent(Deer.class));
        assertNull(options.getOptionsForAgent(Wolf.class));
    }

    @Test
    public void testSetMaxIteration() {
        LifeOptions options = new LifeOptions();
        final int maxIterations = Utils.randomPositiveInteger(100);
        options.setMaximumIterations(maxIterations);
        assertEquals(maxIterations, options.getMaximumIterations());
    }
}