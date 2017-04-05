package gui;

import core.Deer;
import core.Wolf;
import javafx.scene.image.Image;

/**
 * Created by Sami on 05/04/2017.
 */
public class DeerView extends Image {

    public static final String DEER_IMG_64 = "/images/deer@64x64.png";
    private static final double DEFAULT_IMG_WIDTH = 64.0f;
    private static final double DEFAULT_IMG_HEIGHT = 64.0f;

    public DeerView(Deer agent, double width, double height) {
        super(Deer.class.getResource(DEER_IMG_64).toString(), width, height, true, true);
    }
}
