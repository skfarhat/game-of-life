package gui;

import javafx.scene.paint.Color;

/**
 * Created by Sami on 11/04/2017.
 */
public class Utils {

    public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
}
