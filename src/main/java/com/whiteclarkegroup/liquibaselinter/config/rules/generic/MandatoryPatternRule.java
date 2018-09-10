package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.Change;

public class MandatoryPatternRule extends PatternRule {
    public MandatoryPatternRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        return new NotBlankRule(this.getRuleConfig()).invalid((String) object, change) || super.invalid(object, change);
    }
}
