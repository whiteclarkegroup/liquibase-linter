package com.wcg.liquibase.config.rules.generic;

import com.wcg.liquibase.config.rules.Rule;
import com.wcg.liquibase.config.rules.RuleConfig;
import liquibase.change.Change;

public class GenericRule extends Rule {

    public GenericRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        return false;
    }

}
