package gui;

import core.*;
import core.exceptions.LifeException;
import core.exceptions.TooManySurfacesException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ControlPanelController implements Initializable {

    /**
     *  logger
     */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());


    public final String START_BUTTON_TEXT1 = "Start";
    public final String START_BUTTON_TEXT2 = "Pause";
    public Label speedLabel;

    private LifeStarter lifeStarter;
    @FXML private TabPane tabPane;

    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private TextField maxIterationsTextField;
    @FXML private TextField colsTextField;
    @FXML private TextField rowsTextField;
    @FXML private Slider frequencySlider;
    @FXML private Label iterationsLabel;

    private StackedTitledPanes agentsCtrlPane;
    private RulesPane rulesPane;
    private LifeOptions lifeOptions;

    /**
     *
     */
    public ControlPanelController() {}

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

        // General Tab
        tabPane.getTabs().get(0).setText("General");

        // Agents Tab
        tabPane.getTabs().get(1).setText("Agents");
        agentsCtrlPane = new StackedTitledPanes(lifeOptions);
        agentsCtrlPane.setChangeListener( (a,b) -> update(a,b));
        tabPane.getTabs().get(1).setContent(agentsCtrlPane);

        rulesPane = new RulesPane(lifeOptions.getConsumeRules(), lifeOptions.getSupportedAgents());
        tabPane.getTabs().get(2).setContent(rulesPane);
        updateSpeedLabelText();
    }

    private void update(ParamField pf, LifeAgentOptions lap) {
        System.out.println("update called with " + pf + " " + lap);
        if (pf.getName().equals("age")) {
            try {
                int age = validateInteger(pf.getTextField());
                lap.setAgeBy(age);
                pf.setUpdated();
            }
            catch(IllegalArgumentException e) {
                pf.setInvalid();
            }
        }
        else if (pf.getName().equals("reproduction")) {
            try {
                double repro = validateDouble(pf.getTextField());
                lap.setReproductionRate(repro);
                pf.setUpdated();
            }
            catch(IllegalArgumentException e) {
                pf.setInvalid();
            }
        }
        else if (pf.getName().equals("i0")) {
            try {
                int i0 = validatePositiveInteger(pf.getTextField());
                lap.setInitialCount(i0);
                pf.setUpdated();
            }
            catch(IllegalArgumentException e) {
                pf.setInvalid();
            }
        }
        else if (pf.getName().equals("e0")) {
            try {
                int e0 = validatePositiveInteger(pf.getTextField());
                lap.setInitialEnergy(e0);
                pf.setUpdated();
            }
            catch(IllegalArgumentException e) {
                pf.setInvalid();
            }
        }
        else {
            // not handled..
        }

    }

    /**
     * @param actionEvent
     */
    public void startButtonPressed(ActionEvent actionEvent) {
        try {

            Integer maxIterations = validateInput(maxIterationsTextField, Integer.class).intValue();
            Integer gridRows = validateInput(rowsTextField, Integer.class).intValue();
            Integer gridCols = validateInput(colsTextField, Integer.class).intValue();

            lifeOptions.setMaximumIterations(maxIterations);
            lifeOptions.setGridCols(gridCols);
            lifeOptions.setGridRows(gridRows);

            // reset all the status images next to the textfields
            agentsCtrlPane.resetStatusImages();

            // user wants to start a new simulation
            if (State.STOPPED == lifeStarter.getState()) {
                Life life = new Life(lifeOptions);

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
            LOGGER.log(Level.WARNING, "Invalid user input");
            LOGGER.log(Level.FINER, exc.toString(), exc);
        }
        catch(TooManySurfacesException exc) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // alert.setTitle("Error Dialog");
            alert.setHeaderText("Too many surface instances");
            alert.setContentText(exc.getMessage());
            alert.showAndWait();
        }
        catch (LifeException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }

    }

    /**
     * @param actionEvent
     */
    public void stopButtonPressed(ActionEvent actionEvent) {
        if (lifeStarter.stop())
            setStoppedState();
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

    /**  change the name (and other properties?) appearing on the start button */
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
     *  get the speed for the slider (0-1) and adjust the speed label in percent terms
     */
    private void updateSpeedLabelText() {
        double val = getSpeedVal();
        speedLabel.setText(String.format("%.1f%%", val*100));
    }

    private Integer validateInteger(TextField t) throws IllegalArgumentException {
        int val;
        try {
            val = Integer.parseInt(t.getText());
            return val;
        }
        catch(NumberFormatException exc) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid input");

            // extract the name of the textfield from the id
            String fieldName = t.getId().toLowerCase().replaceAll("textfield", "");

            alert.setContentText(String.format("%s: Error parsing input.\n", fieldName));

            t.requestFocus();
            alert.showAndWait();
            throw new IllegalArgumentException();
        }
    }

    private Integer validatePositiveInteger(TextField t) throws IllegalArgumentException {
        int val = validateInteger(t);
        if (val < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid input");

            // extract the name of the textfield from the id
            String fieldName = t.getId().toLowerCase().replaceAll("textfield", "");

            alert.setContentText(String.format("%s: Input cannot be negative.\n", fieldName));

            t.requestFocus();
            alert.showAndWait();
            throw new IllegalArgumentException();
        }
        return val;
    }

    private Double validateDouble(TextField t) throws IllegalArgumentException {
        double val;
        try {
            val = Double.parseDouble(t.getText());
        }
        catch(NumberFormatException exc) {
            throw new IllegalArgumentException();
        }
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
