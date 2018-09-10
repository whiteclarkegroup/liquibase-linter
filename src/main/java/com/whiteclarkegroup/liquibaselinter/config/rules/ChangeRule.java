package com.whiteclarkegroup.liquibaselinter.config.rules;

import liquibase.change.Change;

public interface ChangeRule<T extends Change> {
    String getName();
    Class<T> getChangeType();

    ChangeRule configure(RuleConfig ruleConfig);
    RuleConfig getConfig();

    default boolean supports(T change) {
        return true;
    }
    boolean invalid(T change);
    String getMessage(T change);
}
