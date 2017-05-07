package gui.controllers;

import agents.Deer;
import agents.Grass;
import agents.Wolf;
import core.*;
import core.LifeOptions.ConsumeImplementation;
import core.exceptions.LifeException;
import core.exceptions.TooManySurfacesException;
import gui.*;
import gui.views.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ControlPanelController implements Initializable {

    /**
     *  logger
     */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    public static final String PARAM_FILED_AGE = "age";
    public static final String PARAM_FILED_I0 = "i0";
    public static final String PARAM_FILED_E0 = "e0";
    public static final String PARAM_FILED_REPRO = "reproduction";
    public static final String PARAM_FILED_EGAIN = "egain";
    public static final String PARAM_FILED_ELOSS = "eloss";

    public static final String START_BUTTON_TEXT1 = "Start";
    public static final String START_BUTTON_TEXT2 = "Pause";

    @FXML private TextField consumableCapTextField;
    @FXML private Label speedLabel;
    @FXML private ChoiceBox<ConsumeImplementation> consumeImplementationChoiceBox;
    @FXML private TabPane tabPane;
    @FXML private Button startButton;
    @FXML private Button stopButton;

    private TextField maxIterationsTextField;
    private TextField colsTextField;
    private TextField rowsTextField;

    @FXML private Slider frequencySlider;
    @FXML private Label iterationsLabel;

    private LifeStarter lifeStarter;
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

        consumeImplementationChoiceBox.setItems(
                FXCollections.observableArrayList(
                        ConsumeImplementation.FIXED_ENERGY,
                        ConsumeImplementation.CONSUMABLE_ENERGY,
                        ConsumeImplementation.CAPPED_CONSUMABLE_ENERGY )
        );
        consumeImplementationChoiceBox.setConverter(new StringConverter<ConsumeImplementation>() {
            @Override
            public String toString(ConsumeImplementation object) {
                switch(object) {
                    case FIXED_ENERGY:
                        return "Fixed Energy";
                    case CONSUMABLE_ENERGY:
                        return "Consumable Energy";
                    case CAPPED_CONSUMABLE_ENERGY:
                        return "Capped Consumable Energy";
                }
                return "";
            }

            @Override
            public ConsumeImplementation fromString(String string) {
                return null;
            }
        });
        if (0 != consumeImplementationChoiceBox.getItems().size())
            consumeImplementationChoiceBox.getSelectionModel().select(0);

        consumableCapTextField.setText(String.format("%d", 10));

        try {
            lifeOptions = new LifeOptions(Wolf.class, Deer.class, Grass.class);
            lifeOptions.addConsumeRule(new ConsumeRule(Wolf.class, Deer.class));
            lifeOptions.addConsumeRule(new ConsumeRule(Deer.class, Grass.class));
        }
        catch (LifeException e) {e.printStackTrace();}

        // General Tab
        Tab tab = tabPane.getTabs().get(0);
        VBox generalsVBox = new VBox(10);
        ParamField rowsParamField = new ParamField("rows", "Rows", lifeOptions.getGridRows());
        ParamField colsParamField = new ParamField("cols", "Columns", lifeOptions.getGridCols());
        ParamField iterationsField = new ParamField("iterations", "Iterations", lifeOptions.getMaximumIterations());
        generalsVBox.getChildren().addAll(
                rowsParamField, colsParamField, iterationsField
        );
        rowsTextField = rowsParamField.getInputTextField();
        colsTextField = colsParamField.getInputTextField();
        maxIterationsTextField = iterationsField.getInputTextField();
        tab.setText("General");
        ((Pane)tab.getContent()).getChildren().add(0, generalsVBox);

        // Agents Tab
        tabPane.getTabs().get(1).setText("Agents");
        agentsCtrlPane = new StackedTitledPanes(lifeOptions);
        agentsCtrlPane.setChangeListener( (a,b) -> update(a,b));
        tabPane.getTabs().get(1).setContent(agentsCtrlPane);

        // Rules Tab
        rulesPane = new RulesPane(lifeOptions.getConsumeRules(), lifeOptions.getSupportedAgents());
        tabPane.getTabs().get(2).setContent(rulesPane);

        // Stats Tab
        StatsPane statsPane = new StatsPane(LifeAgent.getStatsCopy(), LifeAgent.getStatsObservable());
        tabPane.getTabs().add(new Tab("Stats", statsPane));

        updateSpeedLabelText();
    }

    private void update(SmartParamField pf, LifeAgentOptions lap) {
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
        else if (pf.getName().equals(PARAM_FILED_REPRO)) {
            try {
                double repro = validateDouble(pf.getTextField());
                lap.setReproductionRate(repro);
                pf.setUpdated();
            }
            catch(IllegalArgumentException e) {
                pf.setInvalid();
            }
        }
        else if (pf.getName().equals(PARAM_FILED_I0)) {
            try {
                int i0 = validateNonNegative(pf.getTextField());
                lap.setInitialCount(i0);
                pf.setUpdated();
            }
            catch(IllegalArgumentException e) {
                pf.setInvalid();
            }
        }
        else if (pf.getName().equals(PARAM_FILED_E0)) {
            try {
                int e0 = validateNonNegative(pf.getTextField());
                lap.setInitialEnergy(e0);
                pf.setUpdated();
            }
            catch(IllegalArgumentException e) {
                pf.setInvalid();
            }
        }
        else if (pf.getName().equals(PARAM_FILED_EGAIN)) {
            try {
                int egain = validateNonNegative(pf.getTextField());
                lap.setEnergyGained(egain);
                pf.setUpdated();
            }
            catch(IllegalArgumentException e) {
                pf.setInvalid();
            }
        }
        else if (pf.getName().equals(PARAM_FILED_ELOSS)) {
            try {
                int eloss = validateNonNegative(pf.getTextField());
                lap.setEnergyLost(eloss);
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
            Integer gridRows = validatePositive(rowsTextField);
            Integer gridCols = validatePositive(colsTextField);
            Integer consumableCap = validatePositive(consumableCapTextField);
            LifeOptions.ConsumeImplementation impl = consumeImplementationChoiceBox.getValue();

            lifeOptions.setMaximumIterations(maxIterations);
            lifeOptions.setGridCols(gridCols);
            lifeOptions.setGridRows(gridRows);
            lifeOptions.setConsumeImplementation(impl);
            lifeOptions.setConsumableEnergyCap(consumableCap);

            // reset all the status images next to the textfields
            agentsCtrlPane.resetStatusImages();

            // user wants to start a new simulation
            if (LifeState.STOPPED == lifeStarter.getState()) {
                Life life = new Life(lifeOptions);

                if (lifeStarter.start(life))
                    setStartedState();
            }
            // user wants to continue the paused simulation
            else if (LifeState.PAUSED == lifeStarter.getState()) {
                lifeStarter.unpause();
                setStartedState();
            }
            // user wants to pause the current simulation
            else if (LifeState.STARTED == lifeStarter.getState()) {
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
    private void changeButtonsState(LifeState state) {
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


    /**
     * displays a UI alert if the value in the TextField is not an integer
     * @param t the textfield that is checked
     * @return the value parsed in the textfield
     * @throws IllegalArgumentException if the value in the textfield is not an integer
     */
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

    /**
     * displays a UI alert if the value in the TextField is not a positive integer (zero is NOT a positive integer)
     * @param t the textfield that is checked
     * @return the value parsed in the textfield
     * @throws IllegalArgumentException if the value in the textfield is not a positive integer
     */
    private Integer validatePositive(TextField t) throws IllegalArgumentException {
        int val = validateInteger(t);
        if (val <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid input");

            // extract the name of the textfield from the id
            String fieldName = t.getId().toLowerCase().replaceAll("textfield", "");

            alert.setContentText(String.format("%s: Input cannot be 0 or negative.\n", fieldName));

            t.requestFocus();
            alert.showAndWait();
            throw new IllegalArgumentException();
        }
        return val;
    }

    /**
     * displays a UI alert if the value in the TextField is negative (0 is acceptable)
     * @param t the textfield that is checked
     * @return the value parsed in the textfield
     * throws IllegalArgumentException if the value in the textfield is negative (0 is NOT NEGATIVE)
     */
    private Integer validateNonNegative(TextField t) throws IllegalArgumentException {
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

    /** enable the textfields that should not be changed when a Life simulation starts */
    private void enableTextFields() {
        boolean disable = false;
        consumableCapTextField.setDisable(disable);
        colsTextField.setDisable(disable);
        rowsTextField.setDisable(disable);
        maxIterationsTextField.setDisable(disable);
    }

    /** disable the textfields that should not be changed when a Life simulation starts */
    private void disableTextFields() {
        boolean disable = true;
        consumableCapTextField.setDisable(disable);
        colsTextField.setDisable(disable);
        rowsTextField.setDisable(disable);
        maxIterationsTextField.setDisable(disable);
    }

    private double getSpeedVal() {
        return frequencySlider.getValue() / 100.0f;
    }
    private void setPausedState() { changeButtonsState(lifeStarter.getState()); }
    private void setStartedState() {
        disableTextFields();
        changeButtonsState(lifeStarter.getState());
    }
    private void setStoppedState() {
        enableTextFields();
        changeButtonsState(lifeStarter.getState());
    }

}
