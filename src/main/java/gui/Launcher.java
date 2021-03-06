/**
 * Created by Sami on 04/04/2017.
 */

package gui;

import core.Life;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher extends Application {

    /**
     *  logger
     */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    public final static String TITLE = "Game of Life";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        // configure logger
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        Handler fh = new FileHandler("%t/game-of-life.log");
        Logger.getLogger("").addHandler(fh);
        Logger.getLogger("com.wombat").setLevel(Level.FINEST);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Root.fxml"));
        Parent root = loader.load();

        stage.setTitle(TITLE);
        stage.setScene(new Scene(root, root.prefWidth(-1), root.prefHeight(-1)));
        stage.show();
    }
}
