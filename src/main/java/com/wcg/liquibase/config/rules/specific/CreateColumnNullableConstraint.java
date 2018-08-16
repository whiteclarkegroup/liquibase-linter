package com.wcg.liquibase.config.rules.specific;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.Rule;
import liquibase.change.Change;
import liquibase.change.ConstraintsConfig;

public class CreateColumnNullableConstraint extends Rule<ConstraintsConfig> {

    public CreateColumnNullableConstraint(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(ConstraintsConfig constraints, Change change) {
        return constraints == null || constraints.isNullable() == null;
    }

}
