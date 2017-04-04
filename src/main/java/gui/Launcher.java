/**
 * Created by Sami on 04/04/2017.
 */

package gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Root.fxml"));
        Parent root = loader.load();

        stage.setTitle("FXML Welcome");
        stage.setScene(new Scene(root, root.prefWidth(-1), root.prefHeight(-1)));
        stage.show();
    }
}
