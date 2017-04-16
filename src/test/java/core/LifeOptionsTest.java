package core;

import core.exceptions.LifeException;
import core.exceptions.LifeImplementationException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void testListOfSupportedAgentsIsImmutable() throws LifeImplementationException {
        LifeOptions options = new LifeOptions(Deer.class);
        options.getSupportedAgents().remove(0);
    }

    @Test
    public void testDefaultMaxIterations() throws LifeImplementationException {
        LifeOptions options = new LifeOptions();
        assertEquals(LifeOptions.DEFAULT_MAX_ITERATIONS,  options.getMaximumIterations());
    }

    @Test
    public void testDefaultColumns() throws LifeImplementationException {
        LifeOptions options = new LifeOptions();
        assertEquals(LifeOptions.DEFAULT_GRID_N,  options.getGridCols());
    }

    @Test
    public void testDefaultRows() throws LifeImplementationException {
        LifeOptions options = new LifeOptions();
        assertEquals(LifeOptions.DEFAULT_GRID_N,  options.getGridRows());
    }

    @Test
    public void testGetOptionsForAgent() throws LifeImplementationException {
        LifeOptions options = new LifeOptions(Deer.class);
        assertNotNull(options.getOptionsForAgent(Deer.class));
    }

    @Test
    public void testSetMaxIteration() throws LifeImplementationException {
        LifeOptions options = new LifeOptions();
        final int maxIterations = Utils.randomPositiveInteger(100);
        options.setMaximumIterations(maxIterations);
        assertEquals(maxIterations, options.getMaximumIterations());
    }

    @Test
    public void testGetConsumeRulesIsUnmodifiable() throws LifeException {
        LifeOptions options = new LifeOptions(Wolf.class, Deer.class, Grass.class);
        options.addConsumeRules(Wolf.class, Deer.class, Grass.class);
    }

    /**
     * add single consume rule
     * @throws LifeException
     */
    @Test
    public void testAddConsumeRule() throws LifeException {
        LifeOptions options = new LifeOptions(Wolf.class, Deer.class);
        boolean success = options.addConsumeRule(Wolf.class, Deer.class);
        assertTrue(success);
    }

    /**
     * add multiple consume rules
     * @throws LifeException
     */
    @Test
    public void testAddConsumeRulesWithArray() throws LifeException {
        LifeOptions options = new LifeOptions(Wolf.class, Deer.class, Grass.class);
        boolean success = options.addConsumeRules(Wolf.class, Deer.class, Grass.class); /// Wolf consumes (Deer and Grass)
        assertEquals(true, success);
    }

    /**
     * add multiple consume rules with List
     * @throws LifeException
     */
    @Test
    public void testAddConsumeRules() throws LifeException {
        List<LifeAgentOptions> list = new ArrayList<>();
        list.add(new LifeAgentOptions(Wolf.class));
        list.add(new LifeAgentOptions(Deer.class));
        list.add(new LifeAgentOptions(Grass.class));

        LifeOptions options = new LifeOptions(list);
        boolean success = options.addConsumeRules(Wolf.class, Deer.class, Grass.class); /// Wolf consumes (Deer and Grass)
        assertTrue(success);
    }

    /**
     * exception when consumes = consumable
     * @throws LifeException
     */
    @Test (expected = LifeException.class)
    public void testExceptionWhenRuleWithSameConsumesAndConsumable() throws LifeException {
        LifeOptions options = new LifeOptions(Wolf.class, Deer.class, Grass.class);
        options.addConsumeRule(Wolf.class, Wolf.class);
    }

    /**
     * unsupported consuming
     * @throws LifeException
     */
    @Test(expected = LifeException.class)
    public void testExceptionThrownAddConsumeRuleOfUnsupportedAgent1() throws LifeException {
        LifeOptions options = new LifeOptions(Deer.class, Wolf.class);
        options.addConsumeRule(Deer.class, Grass.class);
    }

    /**
     * unsupported consumable
     * @throws LifeException
     */
    @Test(expected = LifeException.class)
    public void testExceptionThrownAddConsumeRuleOfUnsupportedAgent2() throws LifeException {
        LifeOptions options = new LifeOptions(Deer.class, Grass.class);
        options.addConsumeRule(Wolf.class, Deer.class);
    }

    /**
     * unsupported consumable and unsupported consuming
     * @throws LifeException
     */
    @Test(expected = LifeException.class)
    public void testExceptionThrownAddConsumeRuleOfUnsupportedAgent3() throws LifeException {
        LifeOptions options = new LifeOptions(Grass.class);
        options.addConsumeRule(Wolf.class, Deer.class);
    }
}