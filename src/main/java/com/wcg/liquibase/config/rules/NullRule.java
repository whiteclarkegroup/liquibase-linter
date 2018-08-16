package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
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
