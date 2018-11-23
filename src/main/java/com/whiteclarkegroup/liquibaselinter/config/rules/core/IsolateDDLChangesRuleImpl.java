package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.ChangeLogLinter;
import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.changelog.ChangeSet;

public class IsolateDDLChangesRuleImpl extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "isolate-ddl-changes";
    private static final String MESSAGE = "Should only have a single ddl change per change set";

    public IsolateDDLChangesRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return changeSet.getChanges().stream().filter(cng -> ChangeLogLinter.DDL_CHANGE_TYPES.contains(cng.getClass())).count() > 1;
    }
}
