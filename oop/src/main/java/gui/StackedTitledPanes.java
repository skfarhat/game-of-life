package gui;

import core.*;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class StackedTitledPanes extends VBox {

    /**
     *  logger
     */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    private LifeOptions options;

    private Map<Class<? extends LifeAgent>, ParamField[]> fieldsMap = new HashMap<>();

    public StackedTitledPanes(LifeOptions options) {
        this.options = options;

        if (options == null)
            return;

        for (Class<? extends LifeAgent> cls : options.getSupportedAgents()){

            VBox vbox = new VBox();
            LifeAgentOptions agentOpts = options.getOptionsForAgent(cls);

            ParamField fields[] = {
                    new ParamField(ControlPanelController.PARAM_FILED_I0, "Initial #", agentOpts.getInitialCount()),
                    new ParamField(ControlPanelController.PARAM_FILED_E0, "Initial Energy", agentOpts.getInitialEnergy()),
                    new ParamField(ControlPanelController.PARAM_FILED_AGE, "Age", agentOpts.getAgeBy()),
                    new ParamField(ControlPanelController.PARAM_FILED_REPRO ,"Reproduction", agentOpts.getReproductionRate()),
                    new ParamField(ControlPanelController.PARAM_FILED_EGAIN ,"Energy Gain", agentOpts.getEnergyGained()),
                    new ParamField(ControlPanelController.PARAM_FILED_ELOSS ,"Energy Loss", agentOpts.getEnergyLost()),
            };

            // disable the EnergyLoss (assumed to be the last field) for Creatures
            if (Creature.class.isAssignableFrom(cls)) {
                fields[fields.length-1].setDisable(true);
            }

            fieldsMap.put(cls, fields);

            vbox.getChildren().addAll(fields);

            String paneName = agentOpts.getAgentType().getSimpleName();
            TitledPane pane = new TitledPane(paneName, vbox);
            getChildren().add(pane);
        }
        ((TitledPane) getChildren().get(0)).setExpanded(true);
    }

    /**
     *  set the change listener on each ParamField created and call the BiConsumer parameter passed
     * @param b functional interface accepting two arguments: (1) ParamField and (2) LifeAgentOptions allowing the
     *          functional implementation to set the correct value to LifeAgentOptions.
     */
    public void setChangeListener(BiConsumer<ParamField, LifeAgentOptions> b) {
        // for each entry in the map
        // and for each ParamField in the ParamField array[]
        // add a listener to setOnAction()
        if (null != b) {
            fieldsMap.entrySet()
                    .stream()
                    .forEach( entry -> {
                        Class<? extends LifeAgent> key = entry.getKey();
                        LifeAgentOptions lap = options.getOptionsForAgent(key);

                        // for every ParamField, when onAction, call the passed BiConsumer
                        Arrays.stream(entry.getValue())
                                .forEach(pf -> pf.setOnAction(ev -> b.accept(pf, lap)));
                    }
            );
        }
    }

    /**  set to null all the ParamField images */
    public void resetStatusImages() {
        fieldsMap.entrySet().stream().forEach(e -> Arrays.stream(e.getValue()).forEach(pf -> pf.resetStatusImage()));
    }
}
