package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;

public class MaxLengthRule extends Rule<String> {

    public MaxLengthRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(String value, Change change) {
        return value != null && value.length() > getRuleConfig().getMaxLength();
    }

    @Override
    String buildErrorMessage(String object, Change change) {
        return String.format(getRuleConfig().getErrorMessage(), object, getRuleConfig().getMaxLength());
    }

}
