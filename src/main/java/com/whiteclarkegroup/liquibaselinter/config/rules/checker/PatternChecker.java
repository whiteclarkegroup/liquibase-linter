package com.whiteclarkegroup.liquibaselinter.config.rules.checker;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;

import java.util.regex.Pattern;

public class PatternChecker {
    private static final String DYNAMIC_VALUE = "{{value}}";

    private final RuleConfig ruleConfig;

    public PatternChecker(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    private Pattern getDynamicPattern(String value) {
        return Pattern.compile(ruleConfig.getPatternString().replace(DYNAMIC_VALUE, value));
    }

    private String getDynamicValue(Object subject) {
        return ruleConfig.getDynamicValueExpression()
            .map(expression -> expression.getValue(subject, String.class))
            .orElse(null);
    }

    public boolean check(String value, Object subject) {
        if (value == null || value.equals("")) {
            return false;
        }
        if (ruleConfig.getPatternString() != null) {
            if (ruleConfig.getPatternString().contains(DYNAMIC_VALUE)) {
                return !getDynamicPattern(getDynamicValue(subject)).matcher(value).matches();
            } else {
                return !ruleConfig.getPattern().map(pattern -> pattern.matcher(value).matches()).orElse(true);
            }
        }
        return false;
    }
}
