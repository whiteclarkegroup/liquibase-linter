package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.DropNotNullConstraintChange;

public class DropNotNullRequireColumnDataTypeRuleImpl extends AbstractLintRule implements ChangeRule<DropNotNullConstraintChange> {
    private static final String NAME = "drop-not-null-require-column-data-type";
    private static final String MESSAGE = "Drop not null constraint column data type attribute must be populated";

    public DropNotNullRequireColumnDataTypeRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<DropNotNullConstraintChange> getChangeType() {
        return DropNotNullConstraintChange.class;
    }

    @Override
    public boolean invalid(DropNotNullConstraintChange dropNotNullConstraintChange) {
        return dropNotNullConstraintChange.getColumnDataType() == null || dropNotNullConstraintChange.getColumnDataType().isEmpty();
    }

}
