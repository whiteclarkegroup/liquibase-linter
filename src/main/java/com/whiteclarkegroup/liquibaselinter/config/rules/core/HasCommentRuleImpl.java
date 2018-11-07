package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.changelog.ChangeSet;

public class HasCommentRuleImpl extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "has-comment";
    private static final String MESSAGE = "Change set must have a comment";

    public HasCommentRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        final String comments = changeSet.getComments();
        return comments == null || comments.isEmpty();
    }
}
