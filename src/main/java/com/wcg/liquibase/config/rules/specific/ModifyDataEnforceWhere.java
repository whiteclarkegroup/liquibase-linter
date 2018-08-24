package com.wcg.liquibase.config.rules.specific;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.Rule;
import liquibase.change.Change;
import liquibase.change.core.AbstractModifyDataChange;

public class ModifyDataEnforceWhere extends Rule<AbstractModifyDataChange> {

    public ModifyDataEnforceWhere(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(AbstractModifyDataChange modifyDataChange, Change change) {
        return getRuleConfig().getRequireWhere().contains(modifyDataChange.getTableName()) && (modifyDataChange.getWhere() == null || modifyDataChange.getWhere().isEmpty());
    }

    @Override
    protected String buildErrorMessage(AbstractModifyDataChange modifyDataChange) {
        return String.format(getRuleConfig().getErrorMessage(), modifyDataChange.getTableName());
    }

}
