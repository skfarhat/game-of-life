package gui;

import core.LifeAgentStats;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Observable;
import java.util.Observer;

import static java.lang.Float.MAX_VALUE;

public class LifeAgentStatsPane extends VBox implements Observer {

    private final Label agentTypeLabel;
    private final Label aliveDeadLabel;
    private final Label createdLabel;
    private final Label reproducedLabel;

    private final LifeAgentStats agentStats;

    public LifeAgentStatsPane(LifeAgentStats s) {
        super(2.0); // spacing
        this.agentStats = s;
        this.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        s.addObserver(this);

        // fixed labels - those don't change
        final Label fAliveDead = new Label("Alive / Dead ");
        final Label fCreatedLabel = new Label("Total: ");
        final Label fReproducedLabel = new Label("Reproduced: ");

        // adaptive labels
        agentTypeLabel = new Label(s.getAgentType().getSimpleName());
        aliveDeadLabel = new Label(String.format("%d / %d", (s.getNbCreated() - s.getNbDied()), s.getNbDied()));
        createdLabel = new Label(stringFromLong(s.getNbCreated()));
        reproducedLabel = new Label(stringFromLong(s.getNbReproduced()));

        // add all children
        getChildren().add(new HBox(agentTypeLabel, new ImageView()));
        getChildren().add(hboxFromLabels(fAliveDead, aliveDeadLabel));
        getChildren().add(hboxFromLabels(fCreatedLabel, createdLabel));
        getChildren().add(hboxFromLabels(fReproducedLabel, reproducedLabel));

    }

    public HBox hboxFromLabels(Label l1, Label l2) {
        HBox hbox = new HBox(l1, l2);
        l1.setFont(Font.getDefault());
        l2.setMaxWidth(MAX_VALUE);
        l2.setAlignment(Pos.CENTER_RIGHT);
        hbox.setHgrow(l2, Priority.ALWAYS);
        l1.setStyle("-fx-font-weight: bold");
//        l1.setFont(new Font("System Bold", 12));
        return hbox;
    }

    public String stringFromLong(long x) {
        return String.format("%d", x);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {

            // because we can't make the UI changes in update() which is called by a non JavaFX thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    updateUI(o, arg);
                }
            });
        }
    }

    private void updateUI(Observable o, Object arg) {
        String str = (String) arg;
        if (str.equals(LifeAgentStats.NOTIFY_CREATED) || str.equals(LifeAgentStats.NOTIFY_DIED)) {
            aliveDeadLabel.setText(String.format("%d / %d", (agentStats.getNbCreated() - agentStats.getNbDied()), agentStats.getNbDied()));
            createdLabel.setText(stringFromLong(agentStats.getNbCreated()));
        }
        else if (str.equals(LifeAgentStats.NOTIFY_REPRODUCED)) {
            reproducedLabel.setText(stringFromLong(agentStats.getNbReproduced()));
        }
    }
}
