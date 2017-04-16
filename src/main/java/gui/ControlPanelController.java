package gui;

import core.*;
import core.exceptions.LifeException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;


/**
 * Created by Sami on 04/04/2017.
 */
public class ControlPanelController implements Initializable {

    public final String START_BUTTON_TEXT1 = "Start";
    public final String START_BUTTON_TEXT2 = "Pause";
    public Label speedLabel;

    private LifeStarter lifeStarter;
    @FXML private TabPane tabPane;

    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private TextField maxIterationsTextField;
    @FXML private TextField rGrassTextField;
    @FXML private TextField rDeerTextField;
    @FXML private TextField rWolfTextField;
    @FXML private TextField iGrassTextField;
    @FXML private TextField iDeerTextField;
    @FXML private TextField iWolfTextField;
    @FXML private TextField gWolfTextField;
    @FXML private TextField gDeerTextField;
    @FXML private TextField gGrassTextField;
    @FXML private TextField stepDecreaseTextField;
    @FXML private TextField colsTextField;
    @FXML private TextField rowsTextField;
    @FXML private Slider frequencySlider;
    @FXML private Label iterationsLabel;

    private LifeOptions lifeOptions;

    /**
     *
     */
    public ControlPanelController() {

    }

    /**
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        frequencySlider.valueProperty().addListener(event -> {
            frequencySliderDone();
        });

        try { lifeOptions = LifeOptions.createDefaultLifeOptions();}
        catch (LifeException e) {e.printStackTrace();}

        tabPane.getTabs().get(0).setText("General");
        tabPane.getTabs().get(1).setText("Agents");


        StackedTitledPanes agentsCtrlPane = new StackedTitledPanes(lifeOptions);
        tabPane.getTabs().get(1).setContent(agentsCtrlPane);



//        // take the default values from those in Life
//        rDeerTextField.setText(new Double(Life.DEFAULT_R_DEER).toString());
//        rWolfTextField.setText(new Double(Life.DEFAULT_R_WOLF).toString());
//        iWolfTextField.setText(new Integer(Life.DEFAULT_I_WOLF).toString());
//        iWolfTextField.setText(new Integer(Life.DEFAULT_I_WOLF).toString());
//        iDeerTextField.setText(new Integer(Life.DEFAULT_I_DEER).toString());
//        iGrassTextField.setText(new Integer(Life.DEFAULT_I_GRASS).toString());

        updateSpeedLabelText();
    }

    /**
     * @param actionEvent
     */
    public void startButtonPressed(ActionEvent actionEvent) {
        try {
            //TODO(sami): check
            Map<String, Number> options = null; //getOptions();

            // user wants to start a new simulation
            if (State.STOPPED == lifeStarter.getState()) {
                Life life = new Life(/* options */ );

                if (lifeStarter.start(life))
                    setStartedState();
            }
            // user wants to continue the paused simulation
            else if (State.PAUSED == lifeStarter.getState()) {
                lifeStarter.unpause();
                setStartedState();
            }
            // user wants to pause the current simulation
            else if (State.STARTED == lifeStarter.getState()) {
                if (lifeStarter.pause())
                    setPausedState();
            }
        }
        catch(IllegalArgumentException exc) {
            System.out.println("Invalid user input");
        } catch (LifeException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param actionEvent
     */
    public void stopButtonPressed(ActionEvent actionEvent) {
        if (lifeStarter.stop())
            setStoppedState();
    }

    /**
     *
     * @return
     * @throws NumberFormatException
     */
    public Map<String, Number> getOptions() throws NumberFormatException {
        Map<String, Number> options = new HashMap<>();

        try {
            double frequency = getSpeedVal();
            lifeStarter.setFrequency(frequency);

            // general params
//            int cols = validateInput(colsTextField, Integer.class).intValue();
//            int rows = validateInput(rowsTextField, Integer.class).intValue();
//            int maxIterations = validateInput(maxIterationsTextField, Integer.class).intValue();
//            int stepDecrease = validateInput(stepDecreaseTextField, Integer.class).intValue();
//            options.put(Life.KEY_MAX_ITERATIONS, maxIterations);
//            options.put(Life.KEY_GRID_COLS, cols);
//            options.put(Life.KEY_GRID_ROWS, rows);
//            options.put(Life.KEY_E_STEP_DECREASE, stepDecrease);
//
//            // initial count params
//            int iWolf = validateInput(iWolfTextField, Integer.class).intValue();
//            int iDeer = validateInput(iDeerTextField, Integer.class).intValue();
//            int iGrass = validateInput(iGrassTextField, Integer.class).intValue();
//            options.put(Life.KEY_I_WOLF, iWolf);
//            options.put(Life.KEY_I_DEER, iDeer);
//            options.put(Life.KEY_I_GRASS, iGrass);
//
//            // reproduction params
//            double rWolf = validateInput(rWolfTextField, Double.class).doubleValue();
//            double rDeer = validateInput(rDeerTextField, Double.class).doubleValue();
//            double rGrass = validateInput(rGrassTextField, Double.class).doubleValue();
//            options.put(Life.KEY_R_WOLF, rWolf);
//            options.put(Life.KEY_R_DEER, rDeer);
//            options.put(Life.KEY_R_GRASS, rGrass);
//
//            // gain params
//            int gDeer = validateInput(gDeerTextField, Integer.class).intValue();
//            int gWolf = validateInput(gWolfTextField, Integer.class).intValue();
//            // int gGrass = validateInput(gGrassTextField, Integer.class).intValue();
//            options.put(Life.KEY_E_DEER_GAIN, gDeer);
//            options.put(Life.KEY_E_WOLF_GAIN, gWolf);

            return options;
        } catch (IllegalArgumentException exc) {
            System.out.println(exc.getMessage());
            throw exc;
        }
    }

    public void setLifeStarter(LifeStarter lifeStarter) {
        this.lifeStarter = lifeStarter;

        // listen to changes on iterations in lifeStarter and adjust the Label
        lifeStarter.getIterationsObservable().addListener((observable, oldVal, newVal) -> {
            int maxIter = lifeStarter.lifeGetter().getMaxIterations();
            String maxStr = (maxIter < 1)? "∞" : String.format("%d", maxIter);
            Platform.runLater(() -> iterationsLabel.setText(String.format("Iterations: %d / %s", newVal, maxStr)));
        });

    }

    public void frequencySliderDone() {

        double val = getSpeedVal();

        lifeStarter.setFrequency(val);

        updateSpeedLabelText();
    }

    /** @brief change the name (and other properties?) appearing on the start button */
    private void changeButtonsState(State state) {
        switch(state) {
            case STOPPED:
                stopButton.setDisable(true);
            case PAUSED:
                startButton.setText(START_BUTTON_TEXT1);
                break;
            case STARTED:
                startButton.setText(START_BUTTON_TEXT2);
                stopButton.setDisable(false);
            default:
                break;
        }
    }

    /**
     * @brief get the speed for the slider (0-1) and adjust the speed label in percent terms
     */
    private void updateSpeedLabelText() {
        double val = getSpeedVal();
        speedLabel.setText(String.format("%.1f%%", val*100));
    }

    /**
     * parses string input in the textfield to the correct Number format. If the string is not a number, an
     * @param t
     * @param c
     * @return the number in the textfield if it satisfied all conditions
     * @throws IllegalArgumentException if the input is not a number, is a negative integer or is a double not between [0-1]
     */
    private Number validateInput(TextField t, Class<? extends Number> c) throws IllegalArgumentException {
        if (Integer.class == c) {
            int val = Integer.MIN_VALUE;
            try {
                val = Integer.parseInt(t.getText());
                if (val < 0)
                    throw new NumberFormatException();
                return val;
            }
            catch(NumberFormatException exc) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                // alert.setTitle("Error Dialog");
                alert.setHeaderText("Invalid input");

                // extract the name of the textfield from the id
                String fieldName = t.getId().toLowerCase().replaceAll("textfield", "");

                if (val == Integer.MIN_VALUE)
                    alert.setContentText(String.format("%s: Error parsing input.\n", fieldName));
                else
                    alert.setContentText(String.format("%s (%d) cannot be negative.\n", fieldName, val));

                t.requestFocus();
                alert.showAndWait();
                throw new IllegalArgumentException();
            }
        }
        else if (Double.class == c) {
            try {
                double val = Double.parseDouble(t.getText());
                if (val < 0 || val > 1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    //            alert.setTitle("Error Dialog");
                    alert.setHeaderText("Invalid input");

                    // strip the string
                    String fieldName = t.getId().toLowerCase().replaceAll("textfield", "");

                    alert.setContentText(String.format("%s (%d) cannot be negative.\n", fieldName, val));
                    alert.showAndWait();
                    throw new IllegalArgumentException();
                }
                return val;
            }
            catch(NumberFormatException exc) {
                throw new IllegalArgumentException();
            }
        }
        else {
            throw new IllegalArgumentException("The parameter c must be Integer or Double");
        }
    }

    private double getSpeedVal() {
        return frequencySlider.getValue() / 100.0f;
    }
    private void setPausedState() { changeButtonsState(lifeStarter.getState()); }
    private void setStartedState() {
        changeButtonsState(lifeStarter.getState());
    }
    private void setStoppedState() { changeButtonsState(lifeStarter.getState()); }

}
