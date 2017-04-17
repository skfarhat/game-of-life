package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ParamField extends HBox {

    private static final String fxmlFilename = "/ParamField.fxml";

    private static final String xSign = "/images/x-sign.png";

    @FXML private TextField inputTextField;
    @FXML private Label paramLabel;
    @FXML private ImageView imageView;

    public ParamField(String paramName, Number nb) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFilename));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        Image img = new Image(xSign);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        imageView.setImage(img);
        imageView.setVisible(false);
        paramLabel.setText(paramName);
        inputTextField.setText(nb.toString());
    }

    public void setInvalid() {
        imageView.setVisible(true);
    }
    public void setValid() {
        imageView.setVisible(false);
    }
    public boolean isValid() {
        return !imageView.isVisible();
    }

}