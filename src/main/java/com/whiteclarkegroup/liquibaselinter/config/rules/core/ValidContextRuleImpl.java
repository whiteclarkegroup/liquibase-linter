package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.ContextExpression;
import liquibase.changelog.ChangeSet;

public class ValidContextRuleImpl extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "valid-context";
    private static final String MESSAGE = "Context does not follow pattern";

    public ValidContextRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        ContextExpression contextExpression = changeSet.getContexts();
        if (contextExpression != null) {
            for (String context : contextExpression.getContexts()) {
                if (checkPattern(context, changeSet)) {
                    return true;
                }
            }
        }
        return false;
    }
}
