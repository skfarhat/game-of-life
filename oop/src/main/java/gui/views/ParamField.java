package gui.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 *
 */
public class ParamField extends HBox {

    private static final String fxmlFilename = "/ParamField.fxml";
    private final String name;

    @FXML protected TextField inputTextField;
    @FXML protected Label paramLabel;

    public ParamField(String name, String paramName, Number nb) {
        this.name = name;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFilename));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        paramLabel.setText(paramName);
        inputTextField.setText(nb.toString());
    }

    public TextField getInputTextField() {
        return inputTextField;
    }
    public String getName() {
        return name;
    }
}
