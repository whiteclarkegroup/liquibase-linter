package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.AbstractModifyDataChange;

public class ModifyDataEnforceWhereImpl extends AbstractLintRule implements ChangeRule<AbstractModifyDataChange> {
    private static final String NAME = "modify-data-enforce-where";
    private static final String MESSAGE = "Modify data on table '%s' must have a where condition";

    public ModifyDataEnforceWhereImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AbstractModifyDataChange> getChangeType() {
        return AbstractModifyDataChange.class;
    }

    @Override
    public boolean invalid(AbstractModifyDataChange modifyDataChange) {
        return modifyDataChange.getWhere() == null || modifyDataChange.getWhere().isEmpty();
    }

    @Override
    public String getMessage(AbstractModifyDataChange change) {
        return formatMessage(change.getTableName());
    }

}
