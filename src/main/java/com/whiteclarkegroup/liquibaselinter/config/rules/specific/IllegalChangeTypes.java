package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.WithFormattedErrorMessage;
import liquibase.change.Change;

public class IllegalChangeTypes extends Rule<Change> implements WithFormattedErrorMessage<Change> {

    public IllegalChangeTypes(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Change change, Change sameChange) {
        if (getRuleConfig().getValues() != null) {
            for (String illegal : getRuleConfig().getValues()) {
                if (change.getClass().getName().equals(illegal)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String formatErrorMessage(String errorMessage, Change change) {
        return String.format(errorMessage, change.getClass().getCanonicalName());
    }
}
