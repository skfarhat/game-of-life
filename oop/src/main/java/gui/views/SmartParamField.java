package gui.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 */
public class SmartParamField extends ParamField {

    private enum State {
        CHANGING,
        UPDATED,
        INVALID
    }

    private static final String fxmlFilename = "/ParamField.fxml";

    private static final String xSign = "/images/x-sign.png";

    private static final String updateSign = "/images/update.png";

    private static final String checkPath= "/images/check@50x50.png";

    private final Image invalidImage = new Image(xSign);
    private final Image changingImage = new Image(updateSign);
    private final Image checkImage = new Image(checkPath);

    private State state;

    private ImageView imageView = new ImageView();


    public SmartParamField(String name, String paramName, Number nb) {
        super(name, paramName, nb);

        imageView.setFitHeight(25.0f);
        imageView.setFitWidth(25.0f);

        inputTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (false == newVal.equals(oldVal)) {
                setChanging();
            }
        });
        getChildren().add(0, imageView);
    }

    public void setInvalid() {
        imageView.setImage(invalidImage);
    }

    public TextField getTextField() {
        return inputTextField;
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

    public void setValid() {
        imageView.setVisible(false);
    }

    public boolean isValid() {
        return !imageView.isVisible();
    }

}