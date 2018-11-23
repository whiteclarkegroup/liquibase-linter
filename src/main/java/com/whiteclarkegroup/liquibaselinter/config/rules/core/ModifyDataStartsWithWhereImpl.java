package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.AbstractModifyDataChange;

public class ModifyDataStartsWithWhereImpl extends AbstractLintRule implements ChangeRule<AbstractModifyDataChange> {
    private static final String NAME = "modify-data-starts-with-where";
    private static final String MESSAGE = "Modify data where starts with where clause, that's probably a mistake";

    public ModifyDataStartsWithWhereImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AbstractModifyDataChange> getChangeType() {
        return AbstractModifyDataChange.class;
    }

    @Override
    public boolean invalid(AbstractModifyDataChange modifyDataChange) {
        return modifyDataChange.getWhere() != null && modifyDataChange.getWhere().toLowerCase().startsWith("where");
    }

}
