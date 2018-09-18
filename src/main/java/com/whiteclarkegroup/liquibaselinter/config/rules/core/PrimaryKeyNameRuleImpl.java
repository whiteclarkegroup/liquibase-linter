package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.AddPrimaryKeyChange;

public class PrimaryKeyNameRuleImpl extends AbstractLintRule implements ChangeRule<AddPrimaryKeyChange> {
    private static final String NAME = "primary-key-name";
    private static final String MESSAGE = "Primary key name is missing or does not follow pattern";

    public PrimaryKeyNameRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AddPrimaryKeyChange> getChangeType() {
        return AddPrimaryKeyChange.class;
    }

    @Override
    public boolean invalid(AddPrimaryKeyChange change) {
        final String constraintName = change.getConstraintName();
        return checkNotBlank(constraintName) || checkPattern(constraintName, change);
    }

    @Override
    public String getMessage(AddPrimaryKeyChange change) {
        return formatMessage(change.getConstraintName(), ruleConfig.getPatternString());
    }
}
