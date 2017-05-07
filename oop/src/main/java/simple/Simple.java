package simple;

import agents.Deer;
import agents.Grass;
import agents.Wolf;
import core.ConsumeRule;
import core.Life;
import core.LifeAgentOptions;
import core.LifeOptions;
import core.actions.Action;
import core.exceptions.LifeException;

import java.util.List;

public class Simple {
    public static void main(String []args) throws LifeException {
        final int nIterations = 20;
        // initialise options
        LifeAgentOptions wolfOpts = new LifeAgentOptions(Wolf.class);
        LifeOptions opts = new LifeOptions(Wolf.class, Deer.class, Grass.class);
        opts.setGridCols(5);
        opts.setGridRows(5);
        opts.addConsumeRule(new ConsumeRule(Wolf.class, Deer.class));
        opts.addConsumeRule(new ConsumeRule(Deer.class, Grass.class));

        // create and run life
        Life life = new Life(opts);
        for (int i = 0; i < nIterations; i++) {
            List<Action> actions = life.step();
            // TODO(sami): stop the logger from automatically logging?
            // we don't print the actions out because the logger already does that now

//            for (Action action : actions)
//                System.out.println(action);
        }
    }
}
