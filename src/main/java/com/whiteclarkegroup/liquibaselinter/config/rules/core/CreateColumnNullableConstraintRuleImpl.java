package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.AbstractChange;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;

import java.util.List;

public class CreateColumnNullableConstraintRuleImpl extends AbstractLintRule implements ChangeRule<AbstractChange> {
    private static final String NAME = "create-column-nullable-constraint";
    private static final String MESSAGE = "Add column must specify nullable constraint";

    public CreateColumnNullableConstraintRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AbstractChange> getChangeType() {
        return AbstractChange.class;
    }

    @Override
    public boolean supports(AbstractChange change) {
        return change instanceof CreateTableChange || change instanceof AddColumnChange;
    }

    @Override
    public boolean invalid(AbstractChange change) {
        ChangeWithColumns changeWithColumns = (ChangeWithColumns) change;
        for (ColumnConfig column : (List<ColumnConfig>) changeWithColumns.getColumns()) {
            final ConstraintsConfig constraints = column.getConstraints();
            if (constraints == null || constraints.isNullable() == null) {
                return true;
            }
        }
        return false;
    }


}
