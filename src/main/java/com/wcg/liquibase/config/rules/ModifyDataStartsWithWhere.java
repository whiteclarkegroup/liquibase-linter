package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;
import liquibase.change.core.AbstractModifyDataChange;

public class ModifyDataStartsWithWhere extends Rule<AbstractModifyDataChange> {

    public ModifyDataStartsWithWhere(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(AbstractModifyDataChange modifyDataChange, Change change) {
        return modifyDataChange.getWhere() != null && modifyDataChange.getWhere().toLowerCase().startsWith("where");
    }

}
