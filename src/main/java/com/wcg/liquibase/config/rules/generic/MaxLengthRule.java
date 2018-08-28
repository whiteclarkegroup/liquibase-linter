package com.wcg.liquibase.config.rules.generic;

import com.wcg.liquibase.config.rules.Rule;
import com.wcg.liquibase.config.rules.RuleConfig;
import com.wcg.liquibase.config.rules.WithFormattedErrorMessage;
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
