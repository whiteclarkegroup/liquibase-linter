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

public class CreateColumnNoDefinePrimaryKeyRuleImpl extends AbstractLintRule implements ChangeRule<AbstractChange> {
    private static final String NAME = "create-column-no-define-primary-key";
    private static final String MESSAGE = "Add column must not use primary key attribute. Instead use AddPrimaryKey change type";

    public CreateColumnNoDefinePrimaryKeyRuleImpl() {
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
        for (ColumnConfig columnConfig : (List<ColumnConfig>) changeWithColumns.getColumns()) {
            ConstraintsConfig constraints = columnConfig.getConstraints();
            if (constraints != null && (Boolean.TRUE.equals(constraints.isPrimaryKey()))) {
                return true;
            }
        }
        return false;
    }

}
