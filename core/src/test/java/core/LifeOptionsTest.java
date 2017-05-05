package core;

import core.exceptions.LifeImplementationException;
import core.exceptions.LifeRuntimeException;
import core.exceptions.TooManySurfacesException;
import org.junit.Test;

import java.util.List;

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

    @Test(expected = LifeRuntimeException.class)
    public void testLifeOptionsWithSameClassTwice() {
        new LifeOptions(Wolf.class, Wolf.class);
    }

    @Test(expected = LifeRuntimeException.class)
    public void testLifeOptionsWithLifeAgentOptionsWithSameClass() {
        LifeAgentOptions o1 = new LifeAgentOptions(Wolf.class);
        LifeAgentOptions o2 = new LifeAgentOptions(Wolf.class);
        new LifeOptions(o1, o2);
    }
}