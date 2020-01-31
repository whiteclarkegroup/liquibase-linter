package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.AbstractModifyDataChange;

import java.util.regex.Pattern;

@AutoService({ChangeRule.class})
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
        return matchesTableName(modifyDataChange.getTableName())
            && (checkNotBlank(modifyDataChange.getWhere()) || checkPattern(modifyDataChange.getWhere(), modifyDataChange));
    }

    @Override
    public String getMessage(AbstractModifyDataChange change) {
        return formatMessage(change.getTableName());
    }

    private boolean matchesTableName(String tableName) {
        return getConfig().getValues().stream().anyMatch(value -> Pattern.compile(value).matcher(tableName).matches());
    }

}
