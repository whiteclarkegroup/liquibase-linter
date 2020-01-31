package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.RawSQLChange;
import liquibase.change.core.SQLFileChange;

@AutoService({ChangeRule.class})
public class NoRawSqlRuleImpl extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "no-raw-sql";
    private static final String MESSAGE = "Raw sql change types not allowed, use appropriate Liquibase change types";

    public NoRawSqlRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<Change> getChangeType() {
        return Change.class;
    }

    @Override
    public boolean invalid(Change change) {
        return change instanceof RawSQLChange || change instanceof SQLFileChange;
    }

}
