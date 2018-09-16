package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractChangeSetRule;
import liquibase.changelog.ChangeSet;

public class NoPreconditionsRuleImpl extends AbstractChangeSetRule {
    private static final String NAME = "no-preconditions";
    private static final String MESSAGE = "Preconditions are not allowed in this project";

    public NoPreconditionsRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return changeSet.getPreconditions() != null;
    }
}
