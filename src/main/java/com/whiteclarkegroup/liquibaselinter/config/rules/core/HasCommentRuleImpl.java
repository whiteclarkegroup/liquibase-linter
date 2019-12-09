package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.change.core.TagDatabaseChange;
import liquibase.changelog.ChangeSet;

@AutoService({ChangeSetRule.class})
public class HasCommentRuleImpl extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "has-comment";
    private static final String MESSAGE = "Change set must have a comment";

    public HasCommentRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        if (changeSet.getChanges().stream().anyMatch(change -> change instanceof TagDatabaseChange)) {
            /*
            https://github.com/whiteclarkegroup/liquibase-linter/issues/90
            tagDatabase changes cannot have any siblings in a changeSet - not even comments
            so we have to make an exception here
             */
            return false;
        }
        return checkNotBlank(changeSet.getComments());
    }
}
