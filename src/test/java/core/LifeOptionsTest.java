package core;

import core.exceptions.LifeException;
import core.exceptions.LifeImplementationException;
import core.exceptions.TooManySurfacesException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LifeOptionsTest {

    @Test
    public void testConstructorWithNullParamWorks() {
        new LifeOptions((LifeAgentOptions[]) null);
    }

    @Test
    public void testConstructorWithNullParamWorks1() {
        new LifeOptions((Class<? extends LifeAgent>[]) null);
    }

    @Test
    public void testConstructorWithNullParamWorks2() {
        new LifeOptions((List<LifeAgentOptions>) null);
    }

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
    public void testListOfSupportedAgentsIsImmutable() throws LifeImplementationException, TooManySurfacesException {
        LifeOptions options = new LifeOptions(Deer.class);
        options.getSupportedAgents().remove(0);
    }

    @Test
    public void testDefaultMaxIterations() throws LifeImplementationException, TooManySurfacesException {
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

    @Test
    public void testSetGridCols() {
        LifeOptions options = new LifeOptions();
        final int cols = Utils.randomPositiveInteger(100);
        options.setGridCols(cols);
        assertEquals(options.getGridCols(), cols);
    }

    @Test
    public void testSetGridRows() {
        LifeOptions options = new LifeOptions();
        final int rows = Utils.randomPositiveInteger(100);
        options.setGridRows(rows);
        assertEquals(options.getGridRows(), rows);
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

    /** make sure the default LifeOptions includes Consume Rules: Wolf consumes Deer and Deer consumes Grass */
    @Test
    public void testCreateDefaultLifeOptions() throws LifeException {
        LifeOptions opts = LifeOptions.createDefaultLifeOptions();

        // support Wolf, Grass and Deer
        assertTrue(opts.getSupportedAgents().contains(Wolf.class));
        assertTrue(opts.getSupportedAgents().contains(Deer.class));
        assertTrue(opts.getSupportedAgents().contains(Grass.class));

        // assert Wolf consumes Deer
        assertTrue(opts.getConsumeRules().containsKey(Wolf.class));
        assertTrue(opts.getConsumeRules().get(Wolf.class).contains(Deer.class));

        // assert Deer consumes Grass
        assertTrue(opts.getConsumeRules().containsKey(Deer.class));
        assertTrue(opts.getConsumeRules().get(Deer.class).contains(Grass.class));

    }
}