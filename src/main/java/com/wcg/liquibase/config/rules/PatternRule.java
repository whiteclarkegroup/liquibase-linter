package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;

import java.util.regex.Pattern;

public class PatternRule extends Rule {

    private static final String DYNAMIC_VALUE = "{{value}}";

    public PatternRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    private Pattern getDynamicPattern(String value) {
        return Pattern.compile(getRuleConfig().getPatternString().replace(DYNAMIC_VALUE, value));
    }

    private String getDynamicValue(Change change) {
        return getRuleConfig().getDynamicValueExpression()
                .map(expression -> expression.getValue(change, String.class))
                .orElse(null);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        String value = (String) object;
        if (getRuleConfig().getPatternString().contains(DYNAMIC_VALUE)) {
            return !getDynamicPattern(getDynamicValue(change)).matcher(value).matches();
        } else return !getRuleConfig().getPattern().map(pattern -> pattern.matcher(value).matches()).orElse(true);
    }

    @Override
    String buildErrorMessage(Object object, Change change) {
        return String.format(getRuleConfig().getErrorMessage(), object);
    }

}
