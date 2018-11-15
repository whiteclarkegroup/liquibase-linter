package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.AddUniqueConstraintChange;

public class UniqueConstraintNameRuleImpl extends AbstractLintRule implements ChangeRule<AddUniqueConstraintChange> {
    private static final String NAME = "unique-constraint-name";
    private static final String MESSAGE = "Unique constraint name does not follow pattern";

    public UniqueConstraintNameRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AddUniqueConstraintChange> getChangeType() {
        return AddUniqueConstraintChange.class;
    }

    @Override
    public boolean invalid(AddUniqueConstraintChange change) {
        return checkMandatoryPattern(change.getConstraintName(), change);
    }

    @Override
    public String getMessage(AddUniqueConstraintChange change) {
        return formatMessage(change.getConstraintName());
    }

}
