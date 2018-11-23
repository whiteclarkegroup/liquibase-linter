package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.CreateIndexChange;

public class CreateIndexNameRuleImpl extends AbstractLintRule implements ChangeRule<CreateIndexChange> {
    private static final String NAME = "create-index-name";
    private static final String MESSAGE = "Index name does not follow pattern";

    public CreateIndexNameRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<CreateIndexChange> getChangeType() {
        return CreateIndexChange.class;
    }

    @Override
    public boolean invalid(CreateIndexChange change) {
        return checkMandatoryPattern(change.getIndexName(), change);
    }

    @Override
    public String getMessage(CreateIndexChange change) {
        return formatMessage(change.getIndexName());
    }

}
