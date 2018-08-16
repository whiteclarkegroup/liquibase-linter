package com.wcg.liquibase.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class RuleConfig {

    private static final String DYNAMIC_VALUE = "{{value}}";

    private final boolean enabled;
    private String errorMessage;
    private final String condition;
    private final String patternString;
    private final String dynamicValue;
    private final List<String> enforceWhere;
    private final Long maxLength;
    private Pattern pattern;
    private Expression conditionExpression;
    private Expression dynamicValueExpression;

    @JsonCreator
    public RuleConfig(@JsonProperty("enabled") boolean enabled,
                      @JsonProperty("errorMessage") String errorMessage,
                      @JsonProperty("pattern") String pattern,
                      @JsonProperty("condition") String condition,
                      @JsonProperty("dynamicValue") String dynamicValue,
                      @JsonProperty("requireWhere") List<String> enforceWhere,
                      @JsonProperty("maxLength") Long maxLength) {
        this.enabled = enabled;
        this.errorMessage = errorMessage;
        this.condition = condition;
        this.patternString = pattern;
        this.dynamicValue = dynamicValue;
        this.enforceWhere = enforceWhere;
        this.maxLength = maxLength;
    }

    private boolean isDynamicPattern() {
        return patternString.contains(DYNAMIC_VALUE);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Optional<Expression> getConditionalExpression() {
        if (conditionExpression == null && condition != null) {
            conditionExpression = new SpelExpressionParser().parseExpression(condition);
        }
        return Optional.ofNullable(conditionExpression);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Optional<Expression> getDynamicValueExpression() {
        if (dynamicValueExpression == null && dynamicValue != null) {
            dynamicValueExpression = new SpelExpressionParser().parseExpression(dynamicValue);
        }
        return Optional.ofNullable(dynamicValueExpression);
    }

    public List<String> getEnforceWhere() {
        return enforceWhere;
    }

    public long getMaxLength() {
        return maxLength;
    }

    public Optional<Pattern> getPattern() {
        if (pattern == null && patternString != null && !isDynamicPattern()) {
            pattern = Pattern.compile(patternString);
        }
        return Optional.ofNullable(pattern);
    }

    public String getPatternString() {
        return patternString;
    }
}
