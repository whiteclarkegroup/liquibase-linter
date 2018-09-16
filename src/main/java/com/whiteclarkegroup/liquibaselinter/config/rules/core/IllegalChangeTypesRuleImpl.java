package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.DatabaseChange;

public class IllegalChangeTypesRuleImpl extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "illegal-change-types";
    private static final String MESSAGE = "Change type '%s' is not allowed in this project";

    public IllegalChangeTypesRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<Change> getChangeType() {
        return Change.class;
    }

    @Override
    public boolean invalid(Change change) {
        if (ruleConfig.getValues() != null) {
            return ruleConfig.getValues().stream()
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
    public String getMessage(Change change) {
        return formatMessage(change.getClass().getCanonicalName());
    }
}
