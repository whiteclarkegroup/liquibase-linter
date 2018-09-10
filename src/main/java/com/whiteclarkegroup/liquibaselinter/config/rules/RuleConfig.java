package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@JsonDeserialize(builder = RuleConfig.RuleConfigBuilder.class)
public class RuleConfig {

    private static final String DYNAMIC_VALUE = "{{value}}";

    private final boolean enabled;
    private final String condition;
    private final String patternString;
    private final String dynamicValue;
    private final List<String> values;
    private final Integer maxLength;
    private final String errorMessage;
    private Pattern pattern;
    private Expression conditionExpression;
    private Expression dynamicValueExpression;

    private RuleConfig(RuleConfigBuilder builder) {
        this.enabled = builder.enabled;
        this.errorMessage = builder.errorMessage;
        this.condition = builder.condition;
        this.patternString = builder.pattern;
        this.dynamicValue = builder.dynamicValue;
        this.values = builder.values;
        this.maxLength = builder.maxLength;
    }

    public static RuleConfig enabled() {
        return RuleConfig.builder().withEnabled(true).build();
    }

    public static RuleConfig disabled() {
        return RuleConfig.builder().withEnabled(false).build();
    }

    public static RuleConfigBuilder builder() {
        return new RuleConfigBuilder();
    }

    private boolean isDynamicPattern() {
        return patternString.contains(DYNAMIC_VALUE);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<String> getValues() {
        return values;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public String getPatternString() {
        return patternString;
    }

    private String getCondition() {
        return condition;
    }

    private String getDynamicValue() {
        return dynamicValue;
    }

    public Optional<Expression> getConditionalExpression() {
        if (conditionExpression == null && condition != null) {
            conditionExpression = new SpelExpressionParser().parseExpression(condition);
        }
        return Optional.ofNullable(conditionExpression);
    }

    public Optional<Expression> getDynamicValueExpression() {
        if (dynamicValueExpression == null && dynamicValue != null) {
            dynamicValueExpression = new SpelExpressionParser().parseExpression(dynamicValue);
        }
        return Optional.ofNullable(dynamicValueExpression);
    }

    public Optional<Pattern> getPattern() {
        if (pattern == null && patternString != null && !isDynamicPattern()) {
            pattern = Pattern.compile(patternString);
        }
        return Optional.ofNullable(pattern);
    }

    public boolean hasPattern() {
        return patternString != null && !patternString.equals("");
    }

    public static class RuleConfigBuilder {
        private boolean enabled = true;
        private String errorMessage;
        private String condition;
        private String pattern;
        private String dynamicValue;
        private List<String> values;
        private Integer maxLength;

        public RuleConfigBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public RuleConfigBuilder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        RuleConfigBuilder withCondition(String condition) {
            this.condition = condition;
            return this;
        }

        public RuleConfigBuilder withPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public RuleConfigBuilder withDynamicValue(String dynamicValue) {
            this.dynamicValue = dynamicValue;
            return this;
        }

        public RuleConfigBuilder withValues(List<String> values) {
            this.values = values;
            return this;
        }

        public RuleConfigBuilder withMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public RuleConfig build() {
            return new RuleConfig(this);
        }
    }
}
