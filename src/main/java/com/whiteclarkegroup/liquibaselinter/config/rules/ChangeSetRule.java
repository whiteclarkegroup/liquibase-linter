package com.whiteclarkegroup.liquibaselinter.config.rules;

import liquibase.changelog.ChangeSet;

public interface ChangeSetRule extends LintRule {
    boolean invalid(ChangeSet changeSet);
    default String getMessage(ChangeSet changeSet) {
        return getMessage();
    }
}
