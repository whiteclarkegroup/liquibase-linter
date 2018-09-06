package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.WithFormattedErrorMessage;
import liquibase.change.Change;
import liquibase.change.DatabaseChange;

public class IllegalChangeTypes extends Rule<Change> implements WithFormattedErrorMessage<Change> {

    public IllegalChangeTypes(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Change change, Change sameChange) {
        if (getRuleConfig().getValues() != null) {
            return getRuleConfig().getValues().stream()
                    .anyMatch(illegal -> getChangeName(change).equals(illegal) || getChangeClassName(change).equals(illegal));
        }
        return false;
    }

    private String getChangeClassName(Change change) {
        return change.getClass().getName();
    }

    private String getChangeName(Change change) {
        return change.getClass().getAnnotation(DatabaseChange.class).name();
    }

    @Override
    public String formatErrorMessage(String errorMessage, Change change) {
        return String.format(errorMessage, change.getClass().getCanonicalName());
    }
}
