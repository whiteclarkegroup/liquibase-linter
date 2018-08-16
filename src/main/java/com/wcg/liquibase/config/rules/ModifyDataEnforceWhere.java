package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;
import liquibase.change.core.AbstractModifyDataChange;

public class ModifyDataEnforceWhere extends Rule<AbstractModifyDataChange> {

    public ModifyDataEnforceWhere(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(AbstractModifyDataChange modifyDataChange, Change change) {
        return getRuleConfig().getEnforceWhere().contains(modifyDataChange.getTableName()) && (modifyDataChange.getWhere() == null || modifyDataChange.getWhere().isEmpty());
    }

    @Override
    String buildErrorMessage(AbstractModifyDataChange modifyDataChange, Change change) {
        return String.format(getRuleConfig().getErrorMessage(), modifyDataChange.getTableName());
    }

}
