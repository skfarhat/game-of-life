package gui;

import core.Grass;
import core.exceptions.AgentIsDeadException;
import javafx.scene.paint.Color;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Sami on 14/04/2017.
 */
public class GrassViewTest {

    @Test
    public void testGrassViewWithZeroEnergyHasColorNull() throws AgentIsDeadException {
        Grass grass = new Grass(1);
        grass.die(); // sets the energy to zero

        GrassView view = new GrassView(grass);
        Color color = view.getColor();
        assertNull(color);
    }

    @Test
    public void testGrassViewWithMinimumEnergyDrawsFirstShade() throws AgentIsDeadException {
        Grass grass = new Grass(GrassView.ENERGY_VALUE_LOWER);
        GrassView view = new GrassView(grass);
        Color colorDrawn = view.getColor();
        Color expectedColor = GrassView.getColorShades().get(0);
        assertEquals(expectedColor, colorDrawn);
    }

    @Test
    public void testGrassViewWithEnergyHigherThanMax() throws AgentIsDeadException {
        Grass grass = new Grass(GrassView.ENERGY_VALUE_UPPER + 100);
        GrassView view = new GrassView(grass);
        Color viewColor = view.getColor();
        List<Color> colors = view.getColorShades();
        Color lastColorInGrassView = colors.get(colors.size()  - 1);

        assertEquals(viewColor, lastColorInGrassView);
    }

    /**
     * If the lists returned by GrassView reference the same list, then changing one will change the other. This test
     * ensures that is not the case.
     */
    @Test
    public void testColorShadesListIsImmutable() {
        List<Color> list1 = GrassView.getColorShades();

        // let's ensure for our sanity that the list has some colors in it - if not let's fail the test (why not?)
        assertTrue(list1.size() > 0);

        // remove an item from the obtained list
        Color removed = list1.remove(0);

        // ensure GrassView's list is unchanged - and not bothered
        List<Color> list2 = GrassView.getColorShades();
        assertTrue(list2.contains(removed)); // make sure the second list
    }
}