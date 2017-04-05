package gui;

import core.AgentIsDeadException;
import core.GridCreationException;
import core.InvalidPositionException;
import core.Life;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Sami on 04/04/2017.
 */
public class ControlPanelController implements Initializable {

    public final String START_BUTTON_TEXT1 = "Start";
    public final String START_BUTTON_TEXT2 = "Pause";

    private LifeStarter lifeStarter;

    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private TextField frequencyTextField;
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

    public ControlPanelController() {

    }

    public void startButtonPressed(ActionEvent actionEvent) {
        System.out.println("startButtonPressed");
        try {
            Map<String, Number> options = getOptions();

            // if working
            switchStartButton(startButton);

            try {
                Life life = new Life(options);
                lifeStarter.start(life);
            } catch (GridCreationException e) {
                e.printStackTrace();
            } catch (AgentIsDeadException e) {
                e.printStackTrace();
            } catch (InvalidPositionException e) {
                e.printStackTrace();
            }
        }
        catch(NumberFormatException exc) {

            System.out.println("exception mate: " + exc.getMessage());
        }
    }

    public void stopButtonPressed(ActionEvent actionEvent) {

    }

    /** @brief change the name (and other properties?) appearing on the start button */
    private void switchStartButton(Button btn) {
        if (btn.getText().equals(START_BUTTON_TEXT1)) {
            btn.setText(START_BUTTON_TEXT2);
        }
        else if (btn.getText().equals(START_BUTTON_TEXT2)) {
            btn.setText(START_BUTTON_TEXT1);
        }
        else {

        }
    }
    /**
     * @brief alert the user about the unparsable string
     * @param txt that is expected to be a number
     * @return the parsed number
     */
    private int alertIfNotInteger(String txt) {
        try { return Integer.parseInt(txt); }
        catch(NumberFormatException exc) {
            // show alert here
            throw exc;
        }
    }
    /**
     * @brief alert the user about the unparsable string
     * @param txt that is expected to be a number
     * @return the parsed number
     */
    private double alertIfNotDouble(String txt) {
        try { return Double.parseDouble(txt); }
        catch(NumberFormatException exc) {
            // show alert here
            throw exc;
        }
    }
    /**
     * @param val that must be non-negative
     * @throws IllegalArgumentException if val is negative
     * @return the passed number
     */
    private int exceptionIfNegative(int val) throws IllegalArgumentException {
        if (val < 0) throw new IllegalArgumentException();
        return val;
    }

    /**
     * @param val that must be in 0-1 range
     * @throws IllegalArgumentException if val is out of the range
     * @return the passed number
     */
    private double exceptionIfDoubleOutOfRange(double val) throws IllegalArgumentException {
        if (val < 0 || val > 1)
            throw new IllegalArgumentException("Double values must be between 0 and 1: " + val + " given.");
        return val;
    }

    public Map<String, Number> getOptions() throws NumberFormatException {
        Map<String, Number> options = new HashMap<>();

        try {
            // general params
            int cols = exceptionIfNegative(alertIfNotInteger(colsTextField.getText()));
            int rows = exceptionIfNegative(alertIfNotInteger(rowsTextField.getText()));
            int freq = exceptionIfNegative(alertIfNotInteger(frequencyTextField.getText()));
            int maxIterations = exceptionIfNegative(alertIfNotInteger(maxIterationsTextField.getText()));
            int stepDecrease = exceptionIfNegative(alertIfNotInteger(stepDecreaseTextField.getText()));
            options.put(Life.KEY_GRID_COLS, cols);
            options.put(Life.KEY_GRID_ROWS, rows);
            options.put(Life.KEY_E_STEP_DECREASE, stepDecrease);

            // initial count params
            int iWolf = exceptionIfNegative(alertIfNotInteger(iWolfTextField.getText()));
            int iDeer = exceptionIfNegative(alertIfNotInteger(iDeerTextField.getText()));
            int iGrass = exceptionIfNegative(alertIfNotInteger(iGrassTextField.getText()));
            options.put(Life.KEY_I_WOLF, iWolf);
            options.put(Life.KEY_I_DEER, iDeer);
            options.put(Life.KEY_I_GRASS, iGrass);

            // reproduction params
            double rWolf = exceptionIfDoubleOutOfRange(alertIfNotDouble(rWolfTextField.getText()));
            double rDeer = exceptionIfDoubleOutOfRange(alertIfNotDouble((rDeerTextField.getText())));
            double rGrass = exceptionIfDoubleOutOfRange(alertIfNotDouble((rGrassTextField.getText())));
            options.put(Life.KEY_R_WOLF, rWolf);
            options.put(Life.KEY_R_DEER, rDeer);
            options.put(Life.KEY_R_GRASS, rGrass);

            // gain params
            int gDeer = exceptionIfNegative(alertIfNotInteger(gDeerTextField.getText()));
            int gWolf = exceptionIfNegative(alertIfNotInteger(gWolfTextField.getText()));
            // int gGrass = alertIfNotInteger(gGrassTextField.getText()); // disabled
            options.put(Life.KEY_E_DEER_GAIN, gDeer);
            options.put(Life.KEY_E_WOLF_GAIN, gWolf);

            return options;
        }
        catch (NumberFormatException exc) {
            // let's run over the test fields and highlight the ones that are mis-formatted
//            return null;
            System.out.println(exc.getMessage());
            throw exc;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initial");
    }

    public void setLifeStarter(LifeStarter lifeStarter) {
        this.lifeStarter = lifeStarter;
    }
}
