package gui.views;

import core.ConsumeRule;
import core.Life;
import core.LifeAgent;
import core.exceptions.InvalidOptionsException;
import core.exceptions.LifeException;
import gui.RulesModifier;
import gui.Utils;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RulePane instance if a child of RulesPane defining a ConsumeRule relationship in LifeOptions
 */
public class RulePane extends HBox {

    /**
     * @brief logger
     */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    /** index identifier of the RulePane, is unique inside one RulesPane */
    private int index = -1;

    private RulesModifier delegate;

    private ConsumeRule consumeRule;

    /** first ComboBox picking the consuming */
    private ComboBox<Class<? extends LifeAgent>> cb1;

    /** second ComboBox picket the consumable */
    private ComboBox<Class<? extends LifeAgent>> cb2;

    /** remove button */
    private Button removeButton;

    /** save button */
    private Button saveButton;

    private StringConverter<Class<? extends LifeAgent>> stringConverter = new StringConverter<Class<? extends LifeAgent>>() {
        @Override
        public String toString(Class<? extends LifeAgent> object) {
            return object.getSimpleName();
        }

        @Override
        public Class<? extends LifeAgent> fromString(String string) {
            return null;
        }
    };

    /**
     * called for an empty RulePane where the ComboBoxes have not been chosen yet
     */
    public RulePane(int index, ObservableList supported) {
        init(index, null, supported);
    }

    /**
     * called when the rule was already created and ComboBoxes need to be pre-filled.
     * @param cr consumeRule
     */
    public RulePane(int index, ConsumeRule cr, ObservableList<Class<? extends LifeAgent>> supported) {
        init(index, cr, supported);
    }

    private void init(int index, ConsumeRule consumeRule, ObservableList<Class<? extends LifeAgent>> supported) {
        this.index = index;
        this.cb1 = new ComboBox(supported);
        this.cb2 = new ComboBox(supported);
        final double buttonImgWidth = 9.0;
        final double buttonImgHeight = 9.0;
        removeButton = new Button("", new ImageView(
                new Image(RulesPane.class.getResource("/images/remove-icon.png").toString(),
                        buttonImgWidth, buttonImgHeight, true, true)));
        removeButton.setOnAction(e -> delegateRemove());

        saveButton= new Button("", new ImageView(
                new Image(RulesPane.class.getResource("/images/check@50x50.png").toString(),
                        buttonImgWidth, buttonImgHeight, true, true)));
        saveButton.setOnAction(e -> delegateSave());

        // must be set after the remove button has been created
        setConsumeRule(consumeRule);

        // set some properties on the HBox
        this.setSpacing(2.0);

        cb1.setStyle("-fx-font: 11px \"System\";");
        cb2.setStyle("-fx-font: 11px \"System\";");

        cb1.setConverter(stringConverter);
        cb2.setConverter(stringConverter);

        cb1.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != oldVal)
                adjustSaveButton();
        });

        cb2.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != oldVal)
                adjustSaveButton();
        });

        if (null != consumeRule) {
            cb1.getSelectionModel().select(consumeRule.getConsumer());
            cb2.getSelectionModel().select(consumeRule.getConsumable());
        }

        getChildren().addAll(cb1, cb2, saveButton, removeButton);

        // set how the children will be layed out
        setHgrow(cb1, Priority.ALWAYS);
        setHgrow(cb2, Priority.ALWAYS);
        setHgrow(saveButton, Priority.NEVER);
        setHgrow(removeButton, Priority.NEVER);
    }

    public void delegateRemove() {
        if (null != delegate)
            delegate.remove(this, consumeRule);
    }

    public void delegateSave() {
        Class<?extends LifeAgent> consumer = cb1.getSelectionModel().getSelectedItem();
        Class<?extends LifeAgent> consumable = cb2.getSelectionModel().getSelectedItem();
        try {
            setConsumeRule(new ConsumeRule(consumer, consumable));
            if (null != delegate)
                delegate.save(this, consumeRule);

            saveButton.setDisable(true);

        } catch (LifeException e) {
            LOGGER.log(Level.SEVERE, "Failed to build ConsumeRule: same consumer-consumable?");
            Utils.showErrorAlert("Failed to save rule", e.getMessage());
        } catch (InvalidOptionsException e) {
            saveButton.setDisable(false);
            // show alert ?
            Utils.showErrorAlert("Failed to save rule", e.getMessage());
        }
    }

    private void setConsumeRule(ConsumeRule cr) {
        this.consumeRule = cr;
        removeButton.setDisable(consumeRule == null);
    }

    private void adjustSaveButton() {
        if (cb1.getSelectionModel().getSelectedIndex() < 0 || cb2.getSelectionModel().getSelectedIndex() < 0) {
            saveButton.setDisable(true);
        }
        else {
            if (consumeRule != null
                    && cb1.getSelectionModel().getSelectedItem().equals(consumeRule.getConsumer())
                    && cb2.getSelectionModel().getSelectedItem().equals(consumeRule.getConsumable())) {
                saveButton.setDisable(true);
            }
            else {
                saveButton.setDisable(false);
            }
        }
    }
    public void setDelegate(RulesModifier delegate) {
        this.delegate = delegate;
    }

    public ConsumeRule getConsumeRule() {
        return consumeRule;
    }
}