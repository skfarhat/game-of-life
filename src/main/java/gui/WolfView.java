package gui;

import core.Wolf;
import javafx.scene.image.Image;

/**
 * Created by Sami on 04/04/2017.
 */
public class WolfView extends Image {

    public static final String WOLF_IMG_64 = "/images/wolf@64x64.png";

    private static final double DEFAULT_IMG_WIDTH = 64.0f;
    private static final double DEFAULT_IMG_HEIGHT = 64.0f;

    private Wolf wolf;

    public WolfView(Wolf wolf, double width, double height) {
        super(Wolf.class.getResource(WOLF_IMG_64).toString(), width, height, true, true);
        this.wolf = wolf;
    }
}
