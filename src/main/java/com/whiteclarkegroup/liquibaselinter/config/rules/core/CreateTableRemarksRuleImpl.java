package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.CreateTableChange;

public class CreateTableRemarksRuleImpl extends AbstractLintRule implements ChangeRule<CreateTableChange> {
    private static final String NAME = "create-table-remarks";
    private static final String MESSAGE = "Create table must contain remark attribute";

    public CreateTableRemarksRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<CreateTableChange> getChangeType() {
        return CreateTableChange.class;
    }

    @Override
    public boolean invalid(CreateTableChange createTableChange) {
        return createTableChange.getRemarks() == null || createTableChange.getRemarks().isEmpty();
    }

}
