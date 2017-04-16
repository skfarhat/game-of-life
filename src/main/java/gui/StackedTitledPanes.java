package gui;

import core.LifeAgent;
import core.LifeAgentOptions;
import core.LifeOptions;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class StackedTitledPanes extends VBox {

    public StackedTitledPanes(LifeOptions options) {

        if (options == null)
            return;

        for (Class<? extends LifeAgent> cls : options.getSupportedAgents()){
            // create vbox
            VBox vbox = new VBox();
            LifeAgentOptions agentOpts = options.getOptionsForAgent(cls);
            ParamField ageField = new ParamField("Age", agentOpts.getAgeBy());
            ParamField reproductionField = new ParamField("Reproduction", agentOpts.getReproductionRate());
            ParamField initialCountField = new ParamField("Initial #", agentOpts.getInitialCount());
            ParamField initialEnergyField = new ParamField("Initial Energy", agentOpts.getInitialEnergy());
            vbox.getChildren().addAll(ageField, reproductionField, initialCountField, initialEnergyField);

            String paneName = agentOpts.getAgentType().getSimpleName();
            TitledPane pane = new TitledPane(paneName, vbox);
            getChildren().add(pane);
        }
        ((TitledPane) getChildren().get(0)).setExpanded(true);
    }
//
//    public StackedTitledPanes(List<LifeAgentOptions> paramsList) {
//
//        for (LifeAgentOptions param : paramsList) {
//
//            // create vbox
//            VBox vbox = new VBox();
//            ParamField ageField = new ParamField("Age");
//            ParamField reproductionField = new ParamField("Reproduction");
//            ParamField initialCountField = new ParamField("Initial #");
//            ParamField initialEnergyField = new ParamField("Initial Energy");
//            vbox.getChildren().addAll(ageField, reproductionField, initialCountField, initialEnergyField);
//
//            String paneName = param.getAgentType().getSimpleName();
//            TitledPane pane = new TitledPane(paneName, vbox);
//            getChildren().add(pane);
//        }
//        ((TitledPane) getChildren().get(0)).setExpanded(true);
//    }
}
