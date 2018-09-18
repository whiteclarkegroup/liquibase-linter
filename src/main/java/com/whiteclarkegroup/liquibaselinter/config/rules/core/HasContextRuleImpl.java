package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.changelog.ChangeSet;

public class HasContextRuleImpl extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "has-context";
    private static final String MESSAGE = "Should have at least one context on the change set";

    public HasContextRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return changeSet.getContexts().isEmpty();
    }
}
