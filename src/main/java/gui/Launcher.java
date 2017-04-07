/**
 * Created by Sami on 04/04/2017.
 */

package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Launcher extends Application {

    public final static String TITLE = "Welcome to the Jungle";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Root.fxml"));
        Parent root = loader.load();

        stage.setTitle(TITLE);
        stage.setScene(new Scene(root, root.prefWidth(-1), root.prefHeight(-1)));
        stage.show();
    }
}
