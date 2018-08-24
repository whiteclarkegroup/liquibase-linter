package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;

public abstract class Rule<T> {

    private final RuleConfig ruleConfig;

    protected Rule(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    protected abstract boolean invalid(T object, Change change);

    protected String buildErrorMessage(T object) {
        return ruleConfig.getErrorMessage();
    }

    public RuleConfig getRuleConfig() {
        return ruleConfig;
    }

    public String getErrorMessage() {
        return getRuleConfig().getErrorMessage();
    }

    public boolean isEnabled() {
        return getRuleConfig().isEnabled();
    }
}
