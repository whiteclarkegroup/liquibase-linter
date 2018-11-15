package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.AbstractChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;

public class TableNameLengthRuleImpl extends AbstractLintRule implements ChangeRule<AbstractChange> {
    private static final String NAME = "table-name-length";
    private static final String MESSAGE = "Table '%s' name must not be longer than %d";

    public TableNameLengthRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AbstractChange> getChangeType() {
        return AbstractChange.class;
    }

    @Override
    public boolean supports(AbstractChange change) {
        return change instanceof CreateTableChange || change instanceof RenameTableChange;
    }

    @Override
    public boolean invalid(AbstractChange change) {
        String tableName = getTableName(change);
        return tableName != null && tableName.length() > getConfig().getMaxLength();
    }

    @Override
    public String getMessage(AbstractChange change) {
        return formatMessage(getTableName(change), getConfig().getMaxLength());
    }

    private String getTableName(AbstractChange change) {
        if (change instanceof CreateTableChange) {
            return ((CreateTableChange) change).getTableName();
        } else {
            return ((RenameTableChange) change).getNewTableName();
        }
    }
}
