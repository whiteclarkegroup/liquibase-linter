package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.AbstractChange;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;

import java.util.List;

public class CreateColumnRemarksRuleImpl extends AbstractLintRule implements ChangeRule<AbstractChange> {
    private static final String NAME = "create-column-remarks";
    private static final String MESSAGE = "Add column must contain remarks";

    public CreateColumnRemarksRuleImpl() {
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
            if (columnConfig.getRemarks() == null || columnConfig.getRemarks().isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
