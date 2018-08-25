package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.core.RenameTableChange;
import liquibase.exception.ChangeLogParseException;

public class RenameTableChangeLinter implements Linter<RenameTableChange> {

    private final TableNameLinter tableNameLinter = new TableNameLinter();

    @Override
    public void lint(RenameTableChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        getTableNameLinter().lintTableName(change.getNewTableName(), change, ruleRunner);
    }

    TableNameLinter getTableNameLinter() {
        return tableNameLinter;
    }

}
