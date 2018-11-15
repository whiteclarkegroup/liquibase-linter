package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.AddForeignKeyConstraintChange;

public class ForeignKeyNameRuleImpl extends AbstractLintRule implements ChangeRule<AddForeignKeyConstraintChange> {
    private static final String NAME = "foreign-key-name";
    private static final String MESSAGE = "Foreign key name is missing or does not follow pattern";

    public ForeignKeyNameRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AddForeignKeyConstraintChange> getChangeType() {
        return AddForeignKeyConstraintChange.class;
    }

    @Override
    public boolean invalid(AddForeignKeyConstraintChange change) {
        final String constraintName = change.getConstraintName();
        return checkMandatoryPattern(constraintName, change);
    }

    @Override
    public String getMessage(AddForeignKeyConstraintChange change) {
        return formatMessage(change.getConstraintName(), ruleConfig.getPatternString());
    }
}
