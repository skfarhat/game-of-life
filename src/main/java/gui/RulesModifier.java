package gui;

import core.ConsumeRule;
import core.exceptions.InvalidOptionsException;

public interface RulesModifier {
    void save(RulePane rp, ConsumeRule cr) throws InvalidOptionsException;

    void remove(RulePane rp, ConsumeRule cr);
}
