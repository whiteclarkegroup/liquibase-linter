package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.whiteclarkegroup.liquibaselinter.config.rules.checker.PatternChecker;
import liquibase.change.Change;

import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractChangeRule<T extends Change> implements ChangeRule<T> {
    private final String name;
    private final String message;
    protected RuleConfig ruleConfig;
    private PatternChecker patternChecker;

    protected AbstractChangeRule(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public abstract Class<T> getChangeType();

    @Override
    public ChangeRule configure(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
        if (ruleConfig.hasPattern()) {
            this.patternChecker = new PatternChecker(ruleConfig);
        }
        return this;
    }

    @Override
    public RuleConfig getConfig() {
        return ruleConfig;
    }

    @Override
    public abstract boolean invalid(T change);

    protected boolean checkNotBlank(String value) {
        return value == null || value.equals("");
    }

    protected boolean checkPattern(String value, Change change) {
        return patternChecker.check(value, change);
    }

    @Override
    public String getMessage(T change) {
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
