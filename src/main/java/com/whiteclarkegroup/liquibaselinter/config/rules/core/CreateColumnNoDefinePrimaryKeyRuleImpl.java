package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;

public class CreateColumnNoDefinePrimaryKeyRuleImpl extends AbstractLintRule implements ChangeRule<AddColumnChange> {
    private static final String NAME = "create-column-no-define-primary-key";
    private static final String MESSAGE = "Add column must not use primary key attribute. Instead use AddPrimaryKey change type";

    public CreateColumnNoDefinePrimaryKeyRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AddColumnChange> getChangeType() {
        return AddColumnChange.class;
    }

    @Override
    public boolean invalid(AddColumnChange addColumnChange) {
        for (ColumnConfig columnConfig : addColumnChange.getColumns()) {
            ConstraintsConfig constraints = columnConfig.getConstraints();
            if (constraints != null && (Boolean.TRUE.equals(constraints.isPrimaryKey()))) {
                return true;
            }
        }
        return false;
    }

}
