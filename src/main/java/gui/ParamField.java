package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;


public class ParamField extends HBox {

    private static final String fxmlFilename = "/ParamField.fxml";

    @FXML private TextField inputTextField;
    @FXML private Label paramLabel;


    public ParamField(String paramName, Number nb) {
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


//    public String getText() {
//        return textProperty().get();
//    }
//
//    public void setText(String value) {
//        textProperty().set(value);
//    }

//    public StringProperty textProperty() {
//        return textField.textProperty();
//    }

//    @FXML
//    protected void doSomething() {
//        System.out.println("The button was clicked!");
//    }
}