package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeLogRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.precondition.core.PreconditionContainer;

public class NoPreconditionsRuleImpl extends AbstractLintRule implements ChangeSetRule, ChangeLogRule {
    private static final String NAME = "no-preconditions";
    private static final String MESSAGE = "Preconditions are not allowed in this project";

    public NoPreconditionsRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return changeSet.getPreconditions() != null && !changeSet.getPreconditions().getNestedPreconditions().isEmpty();
    }

    @Override
    public boolean invalid(DatabaseChangeLog changeLog) {
        PreconditionContainer preconditions = changeLog.getPreconditions();
        return preconditions != null
            && (!preconditions.getNestedPreconditions().isEmpty() && !preconditions.getNestedPreconditions().stream().allMatch(pre -> pre instanceof PreconditionContainer));
    }
}
