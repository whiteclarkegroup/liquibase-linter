package com.wcg.liquibase.config.rules.specific;

import com.wcg.liquibase.config.rules.Rule;
import com.wcg.liquibase.config.rules.RuleConfig;
import com.wcg.liquibase.config.rules.WithFormattedErrorMessage;
import liquibase.change.Change;
import liquibase.change.core.AbstractModifyDataChange;

public class ModifyDataEnforceWhere extends Rule<AbstractModifyDataChange> implements WithFormattedErrorMessage<AbstractModifyDataChange> {

    public ModifyDataEnforceWhere(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(AbstractModifyDataChange modifyDataChange, Change change) {
        return getRuleConfig().getRequireWhere().contains(modifyDataChange.getTableName()) && (modifyDataChange.getWhere() == null || modifyDataChange.getWhere().isEmpty());
    }

    @Override
    public String formatErrorMessage(String errorMessage, AbstractModifyDataChange modifyDataChange) {
        return String.format(errorMessage, modifyDataChange.getTableName());
    }
}
