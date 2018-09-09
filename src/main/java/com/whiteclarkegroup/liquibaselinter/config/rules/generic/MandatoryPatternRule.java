package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.WithFormattedErrorMessage;
import liquibase.change.Change;

import java.util.Optional;
import java.util.regex.Pattern;

public class MandatoryPatternRule extends PatternRule {
    public MandatoryPatternRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        return new NotBlankRule(this.getRuleConfig()).invalid((String) object, change) || super.invalid(object, change);
    }
}
