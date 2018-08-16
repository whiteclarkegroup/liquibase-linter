package com.wcg.liquibase.config.rules.generic;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.Rule;
import liquibase.change.Change;

public class NullRule extends Rule {

    public NullRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        return object != null;
    }

}
