package com.wcg.liquibase.config.rules.specific;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.Rule;
import liquibase.change.Change;
import liquibase.change.ConstraintsConfig;

public class CreateColumnNoDefinePrimaryKey extends Rule<ConstraintsConfig> {

    public CreateColumnNoDefinePrimaryKey(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(ConstraintsConfig constraints, Change change) {
        return constraints == null || (constraints.isPrimaryKey() != null && constraints.isPrimaryKey());
    }

}
