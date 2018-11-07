package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.AddColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;

public class CreateColumnNullableConstraintRuleImpl extends AbstractLintRule implements ChangeRule<AddColumnChange> {
    private static final String NAME = "create-column-nullable-constraint";
    private static final String MESSAGE = "Add column must specify nullable constraint";

    public CreateColumnNullableConstraintRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AddColumnChange> getChangeType() {
        return AddColumnChange.class;
    }

    @Override
    public boolean invalid(AddColumnChange change) {
        for (AddColumnConfig column : change.getColumns()) {
            final ConstraintsConfig constraints = column.getConstraints();
            if (constraints == null || constraints.isNullable() == null) {
                return true;
            }
        }
        return false;
    }

}
