package gui.views;

import agents.Grass;
import gui.Utils;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GrassView extends Pane {

    /**  value of energy that will have the lightest shade of color */
    public static int ENERGY_VALUE_LOWER = 1;

    /**  value of energy that will have the darkest shade of color */
    public static int ENERGY_VALUE_UPPER = 99;

    /**  the range in which energies have the same color [0, 10[ have the same color and [10, 20[ have the next shade */
    public static int ENERGY_INTERVAL = 1;

    /**  the first color from which all the others will derive */
    private static final Color firstColor = Color.LIGHTGREEN;
    /**
     *  the number of different shades
     * e.g. when upper = 1000, and lower = 0 and interval = 10
     * we would have (1000 - 0) / 10 = 100 shades of color
     * */
    public static int COLOR_SHADE_COUNT = (ENERGY_VALUE_UPPER - ENERGY_VALUE_LOWER + 1) / ENERGY_INTERVAL;

    /**  list holding all the different shades of color. The array is initialised statically and cannot be modified. */
    private final static List<Color> colorShades;

    static {
        colorShades = new ArrayList<>(COLOR_SHADE_COUNT);
        colorShades.add(firstColor); // the first shade of color
        final double BRIGHTNESS_FACTOR = 1.0 - 1.0 / COLOR_SHADE_COUNT; // the higher it is, the lower the brightness difference between successive shades
        for (int i = 1; i < COLOR_SHADE_COUNT; i++) {
            Color newColor = colorShades.get(i-1).deriveColor(0, 1.0, BRIGHTNESS_FACTOR, 1.0);
            colorShades.add(newColor);
        }
    }
    /** @return a copy of the shades array */
    public static List<Color> getColorShades() {
        ArrayList<Color> copyList = new ArrayList<>();
        copyList.addAll(colorShades);
        return copyList;
    }

    /**  the color with which this Pane is painted */
    public Color color;

    private Grass grass;

    /**
     *  constructor
     *
     * sets the background of the pane based on the energy level of the grass. When the grass' energy level exceeds
     * ENERGY_VALUE_UPPER then the last (darkest) shade of color is drawn. If the grass' energy level is under ENERGY_VALUE_LOWER
     * then no color is drawn and the field color is set to null.
     *
     * @param grass
     */
    public GrassView(Grass grass) {
        super();
        this.grass = grass;

        if (grass.getEnergy() < ENERGY_VALUE_LOWER) {
            // if agents.Grass' energy is zero we don't draw anything. Generally we hope not to get passed agents.Grass with zero energy
            color =  null;
        }
        else {
            // conceptually not the actual energy of the grass but the energy value it will be painted with
            int paintedEnergy = Math.min(Math.min(grass.getEnergy(), ENERGY_VALUE_UPPER), Math.max(grass.getEnergy(), ENERGY_VALUE_LOWER)) ;
            int shadeIndex = (COLOR_SHADE_COUNT * paintedEnergy / ENERGY_VALUE_UPPER) - 1;
            color = colorShades.get(shadeIndex);
            setStyle("-fx-background-color: " + Utils.toRGBCode(color));
        }
    }

    /**
     * @return the color used in the background setting of this pane
     */
    public Color getColor() {
        return color;
    }

}
