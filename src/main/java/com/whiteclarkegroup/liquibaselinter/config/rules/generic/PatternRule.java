package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.WithFormattedErrorMessage;
import liquibase.change.Change;

import java.util.regex.Pattern;

public class PatternRule extends Rule implements WithFormattedErrorMessage {

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
        final String value = (String) object;
        if (getRuleConfig().getPatternString() != null) {
            if (getRuleConfig().getPatternString().contains(DYNAMIC_VALUE)) {
                return !getDynamicPattern(getDynamicValue(change)).matcher(value).matches();
            } else {
                return !getRuleConfig().getPattern().map(pattern -> pattern.matcher(value).matches()).orElse(true);
            }
        }
        return false;
    }

    @Override
    public String formatErrorMessage(String errorMessage, Object object) {
        return String.format(errorMessage, object);
    }
}
