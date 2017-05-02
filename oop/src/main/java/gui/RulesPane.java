package gui;

import core.ConsumeRule;
import core.ConsumeRules;
import core.Life;
import core.LifeAgent;
import core.exceptions.InvalidOptionsException;
import core.exceptions.LifeImplementationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class RulesPane extends VBox implements RulesModifier {

    /**
     * @brief logger
     */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    private ConsumeRules consumeRules;

    /**
     * this pane is the last one added using the addButton and that has not been saved to consumeRules,
     * it can be null (when the addButton was not pressed) or when the new rule has just been saved
     */
    private RulePane newRulePane;

    private Button addButton;

    private ObservableList<Class<? extends LifeAgent>> supported;

    private Map<RulePane, ConsumeRule> childMap = new HashMap<>();

    /**
     * increments whenever a RulePane is created and is passed to the RulePane as index identifier to it. Each RulePane
     * in a RulesPane must have a unique index. Note that consecutive RulePanes on the ui may not have continuous (and contiguous)
     * indices.
     */
    private int rulesPaneCounter = 0;

    /**
     * constructor
     * @param consumeRules
     * @param s
     */
    public RulesPane(ConsumeRules consumeRules, List<Class<?extends LifeAgent>> s) {
        this.consumeRules = consumeRules;
        this.supported = FXCollections.observableArrayList(s);

        // properties
        this.setAlignment(Pos.TOP_CENTER); // top vertically and center horizontally
        this.setSpacing(5.0);
        this.setPadding(new Insets(5.0, 2.0, 5.0, 2.0));

        Iterator<ConsumeRule> it = consumeRules.iterator();
        while(it.hasNext()) {

            // a new RulePane is passed a copy of the ConsumeRule that they can modify, and pass back to us on save, which is
            // when we apply the changes.
            ConsumeRule cr = it.next();
            ConsumeRule copy = new ConsumeRule(cr); // create a copy
            RulePane rp = createRulePane(copy);
            childMap.put(rp, copy);
            getChildren().add(rp);
        }

        // add explaining Label
        final String explanation = "Consumer -> Consumable";
        Label infoLabel = new Label(explanation);
        getChildren().add(0, infoLabel);

        // add the "add" button
        addButton = new Button("+");
        addButton.setOnAction(e -> addRuleEmptyPane());
        getChildren().add(addButton);

        // call this after the add button is created
        setNewRulePane(null);
    }

    /**
     * convenience function returning a RulePane with the index set appropriately. If a pre-filled RulePane should be createed and
     * returned then consumeRule should be non-null.
     * @param cr consumeRule to be passed to the rulePane to be created. The consumeRule passed here is generally a copy of the original which
     *           makes it safe to change.
     * @return RulePane instance
     */
    private RulePane createRulePane(ConsumeRule cr) {
        RulePane rp;
        if (null == cr)
            rp = new RulePane(rulesPaneCounter++, supported);
        else
            rp = new RulePane(rulesPaneCounter++, cr, supported);

        rp.setDelegate(this);
        return rp;
    }

    /**
     * the passed ConsumeRule is a copy of the real one, or we need to make a copy of it before putting in childMap
     * @param rp
     * @param copy
     * @return
     * @throws InvalidOptionsException thrown if the ConsumeRule already exists
     */
    @Override
    public void save(RulePane rp, ConsumeRule copy) throws InvalidOptionsException {

        if (copy == null) {
            // throw runtime exception not InvalidOptionsException
            LOGGER.log(Level.SEVERE, "Received null ConsumeRule to save. Ignoring.");
            throw new LifeImplementationException("Received null ConsumeRule to save. Ignoring.");
        }

        // create a new rule from the copy, this is the one that will be put in childMap and added to consumeRules
        ConsumeRule newRule = new ConsumeRule(copy);

        // get the ConsumeRule associated with the RulePane
        // if none exist (oldRule = null), then we will create a fresh new ConsumeRule
        // otherwise, we have to remove the oldRule from ConsumeRules aznd replace it with the new one
        ConsumeRule oldRule = childMap.get(rp);

        // prevent adding a rule that already exists
        if (true == consumeRules.contains(newRule))
            throw new InvalidOptionsException("Rule already exists!");

        // remove the old rule
        if (null != oldRule) {
            consumeRules.remove(oldRule);
        }

        // add the new rule to consumeRules and update the map
        boolean check = consumeRules.add(newRule);
        childMap.put(rp, newRule);


        if (false == check) {
            LOGGER.log(Level.SEVERE, "Failed to add new rule to ConsumeRules");
        }

        // interface updates
        if (rp == newRulePane) {
            // if this update came from a newRulePane, we need to make 'rp' no longer new.
            setNewRulePane(null);
        }

        // TODO(sami): remove debug info
        System.out.println("save: ConsumeRules: ");
        for (ConsumeRule c : consumeRules)
            System.out.println(c);
    }

    @Override
    public void remove(RulePane rp, ConsumeRule cr) {

        // get the rule associated with the RulePane calling 'remove'
        // if the rule exists (not null), we have to remove it from ConsumeRules
        // if it is null, then all we need to do is remove the RulePane
        ConsumeRule oldRule = childMap.get(rp);

        if (null != oldRule) {
            consumeRules.remove(oldRule);
        }

        childMap.remove(rp);
        getChildren().remove(rp);

        // interface adjustments
        if (rp == newRulePane) {
            // if this was the newRulePane, we need to update the newRulePane instance to null
            setNewRulePane(null);

            // rp can't be the newRulePane and have an oldRule associated with it in childMap, there would be something
            // wrong in the implementation.
            assert null != oldRule :  "newRulePane should not have a ConsumeRule in childMap";
        }

        // TODO(sami): remove debug info
        System.out.println("remove: ConsumeRules: ");
        for (ConsumeRule c : consumeRules)
            System.out.println(c);
    }

    /**
     *
     */
    private void addRuleEmptyPane() {
        if (newRulePane == null) {
            setNewRulePane(createRulePane(null));
            getChildren().add(getChildren().size()-1, newRulePane);
        }
    }

    private void setNewRulePane(RulePane rp) {
        this.newRulePane = rp;
        addButton.setDisable(newRulePane != null);
    }

}
