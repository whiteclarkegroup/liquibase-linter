package com.whiteclarkegroup.liquibaselinter.config.rules;

import liquibase.change.Change;

public interface ChangeRule<T extends Change> extends LintRule {
    Class<T> getChangeType();
    default boolean supports(T change) {
        return true;
    }
    boolean invalid(T change);
    default String getMessage(T change) {
        return getMessage();
    }
}
