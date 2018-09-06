package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.generic.PatternRule;
import liquibase.change.Change;

import java.util.Collection;

public class ValidContext extends PatternRule {

    public ValidContext(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        Collection<String> contexts = (Collection<String>) object;
        for (String context : contexts) {
            if (super.invalid(context, change)) {
                return true;
            }
        }
        return false;
    }

}
