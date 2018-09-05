package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.Change;

public class NotNullRule extends Rule {

    public NotNullRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        return object == null;
    }

}
