package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import liquibase.change.core.RenameTableChange;
import liquibase.exception.ChangeLogParseException;

public class RenameTableChangeLinter implements Linter<RenameTableChange> {

    private TableNameLinter tableNameLinter = new TableNameLinter();

    @Override
    public void lint(RenameTableChange change, Rules rules) throws ChangeLogParseException {
        getTableNameLinter().lintTableName(change.getNewTableName(), change, rules);
    }

    TableNameLinter getTableNameLinter() {
        return tableNameLinter;
    }

}
