package gui;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private static final String updateSign = "/images/update.png";

    private static final String checkPath= "/images/check.png";

    private final Image invalidImage = new Image(xSign);
    private final Image changingImage = new Image(updateSign);
    private final Image checkImage = new Image(checkPath);


    enum State {
        CHANGING,
        UPDATED,
        INVALID
    };

    private State state;

    @FXML
    private TextField inputTextField;
    @FXML
    private Label paramLabel;
    @FXML
    private ImageView imageView;

    private final StringProperty textProperty;
    private String name;

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

        imageView.setImage(null);
        paramLabel.setText(paramName);
        inputTextField.setText(nb.toString());
        textProperty = inputTextField.textProperty();

        inputTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (false == newVal.equals(oldVal)) {
                setChanging();
            }
        });
    }

    public void setInvalid() {
        imageView.setImage(invalidImage);
    }

    public void setValid() {
        imageView.setVisible(false);
    }

    public boolean isValid() {
        return !imageView.isVisible();
    }

    public TextField getTextField() {
        return inputTextField;
    }

    public String getText() {
        return textProperty.get();
    }

    public void setChanging() {
        if (State.CHANGING != state) {
            state = State.CHANGING;
            imageView.setImage(changingImage);
        }
    }

    public void setUpdated() {
        if (State.UPDATED!= state) {
            state = State.UPDATED;
            imageView.setImage(checkImage);
        }
    }

    public void setOnAction(EventHandler<ActionEvent> ev) {
        inputTextField.setOnAction(ev);
    }

    /**  set the image in imageView to null */
    public void resetStatusImage() {
        imageView.setImage(null);
    }

    public String getName() {
        return name;
    }
}