package gui;

import core.Deer;
import core.LifeAgent;
import core.Wolf;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Created by Sami on 04/04/2017.
 */
public class WolfView extends Pane {

    public static final String WOLF_IMG_64 = "/images/wolf@64x64.png";

    private static final double DEFAULT_IMG_WIDTH = 64.0f;
    private static final double DEFAULT_IMG_HEIGHT = 64.0f;

    private Wolf wolf;

    public WolfView(Wolf wolf, double width, double height) {
        super();
        this.wolf = wolf;
        ImageView imgView = new ImageView(new Image(Wolf.class.getResource(WOLF_IMG_64).toString(), width, height, true, true));
        Label energyLabel = new Label(((LifeAgent)wolf).getEnergy().toString());
        getChildren().addAll(imgView, energyLabel);
    }
}
