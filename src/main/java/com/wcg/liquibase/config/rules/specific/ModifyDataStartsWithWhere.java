package com.wcg.liquibase.config.rules.specific;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.Rule;
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
