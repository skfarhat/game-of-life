package gui;

import core.Deer;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class DeerView extends Pane {

    public static final String DEER_IMG_64 = "/images/deer@64x64.png";
    private static final double DEFAULT_IMG_WIDTH = 64.0f;
    private static final double DEFAULT_IMG_HEIGHT = 64.0f;
    private Deer deer;

    public DeerView(Deer deer, double width, double height) {
        super();
        this.deer = deer;
        ImageView imgView = new ImageView(new Image(Deer.class.getResource(DEER_IMG_64).toString(), width, height, true, true));
        Label energyLabel = new Label(deer.getEnergy().toString());
        getChildren().addAll(imgView/*, energyLabel */);
    }
}
