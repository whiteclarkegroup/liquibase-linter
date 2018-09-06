package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
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
