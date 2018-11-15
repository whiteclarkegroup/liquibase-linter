package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.whiteclarkegroup.liquibaselinter.config.rules.checker.PatternChecker;

import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractLintRule implements LintRule {
    private final String name;
    private final String message;
    protected RuleConfig ruleConfig;
    private PatternChecker patternChecker;

    protected AbstractLintRule(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void configure(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
        if (ruleConfig.hasPattern()) {
            this.patternChecker = new PatternChecker(ruleConfig);
        }
    }

    @Override
    public RuleConfig getConfig() {
        return ruleConfig;
    }

    protected boolean checkNotBlank(String value) {
        return value == null || value.equals("");
    }

    protected boolean checkPattern(String value, Object subject) {
        return patternChecker.check(value, subject);
    }

    protected boolean checkMandatoryPattern(String value, Object subject) {
        return value != null && patternChecker.check(value, subject);
    }

    @Override
    public String getMessage() {
        return getMesageTemplate();
    }

    private String getMesageTemplate() {
        return Optional.ofNullable(ruleConfig.getErrorMessage()).orElse(message);
    }

    protected String formatMessage(Object... stuff) {
        return String.format(getMesageTemplate(), Arrays.stream(stuff)
            .map(thing -> Optional.ofNullable(thing).orElse(""))
            .toArray());
    }
}
