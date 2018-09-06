package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.WithFormattedErrorMessage;
import liquibase.change.Change;

public class MaxLengthRule extends Rule<String> implements WithFormattedErrorMessage<String> {

    public MaxLengthRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(String value, Change change) {
        return value != null && value.length() > getRuleConfig().getMaxLength();
    }

    @Override
    public String formatErrorMessage(String errorMessage, String object) {
        return String.format(errorMessage, object, getRuleConfig().getMaxLength());
    }
}
