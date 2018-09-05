package com.whiteclarkegroup.liquibaselinter.config.rules;

import liquibase.change.Change;

public abstract class Rule<T> {

    private final RuleConfig ruleConfig;

    protected Rule(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    protected abstract boolean invalid(T object, Change change);

    public RuleConfig getRuleConfig() {
        return ruleConfig;
    }

    public boolean isEnabled() {
        return getRuleConfig().isEnabled();
    }

    public String getErrorMessage() {
        return getRuleConfig().getErrorMessage();
    }
}
