package core;

import java.util.List;

/**
 * @interface to expose some getter methods in Life without allowing full control
 */
public interface LifeGetter {

    List<Agent> getAgents();

    int getIteration();

    int getMaxIterations();

    Grid<LifeCell> getGrid();

    int getGridRows();

    int getGridCols();
}
