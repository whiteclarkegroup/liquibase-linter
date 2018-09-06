package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
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
