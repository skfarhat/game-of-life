package gui;

import core.Grass;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class GrassView extends Pane {

    /** @brief value of energy that will have the lightest shade of green */
    public static int ENERGY_VALUE_LOWER = 0;

    /** @brief value of energy that will have the darkest shade of green */
    public static int ENERGY_VALUE_UPPER = 100;

    /** @brief the number of different shades */
    public static int GREEN_SHADE_COUNT = 10;

    /** @brief list holding all the different shades of green. The array is initialised statically */
    public static List<Color> greenShades;

    static {
        greenShades = new ArrayList<>(GREEN_SHADE_COUNT);
        greenShades.add(Color.LIGHTGREEN); // the first shade of green
        for (int i = 1; i < GREEN_SHADE_COUNT; i++) {
            double BRIGHTNESS_FACTOR = 0.9; // the higher it is, the lower the brightness difference between successive shades
            Color newColor = greenShades.get(i-1).deriveColor(0, 1.0, BRIGHTNESS_FACTOR, 1.0);
            greenShades.add(newColor);
        }
    }

    private Grass grass;

    public GrassView(Grass grass) {
        super();
        this.grass = grass;
        // conceptually not the actual energy of the grass but the energy value it will be painted with
        int paintedEnergy = Math.max(Math.min(grass.getEnergy(), ENERGY_VALUE_UPPER), Math.max(grass.getEnergy(), ENERGY_VALUE_LOWER)) ;
        int shadeIndex = paintedEnergy / GREEN_SHADE_COUNT;
        setStyle("-fx-background-color: " + Utils.toRGBCode(greenShades.get(shadeIndex)));
    }
}
