package com.whiteclarkegroup.liquibaselinter.config.rules;

import liquibase.changelog.ChangeSet;

public interface ChangeSetRule extends LintRule {
    boolean invalid(ChangeSet changeSet);
    String getMessage(ChangeSet changeSet);
}
