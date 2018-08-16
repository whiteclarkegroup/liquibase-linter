package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.core.CreateTableChange;
import liquibase.exception.ChangeLogParseException;

public class CreateTableChangeLinter implements Linter<CreateTableChange> {

    private ColumnConfigLinter columnConfigLinter = new ColumnConfigLinter();
    private TableNameLinter tableNameLinter = new TableNameLinter();

    @Override
    public void lint(CreateTableChange change, Rules rules) throws ChangeLogParseException {
        RuleRunner.forChange(change).run(rules.getCreateTableRemarks(), change.getRemarks());
        getTableNameLinter().lintTableName(change.getTableName(), change, rules);
        getColumnConfigLinter().lintColumnConfig(change, rules);
    }

    ColumnConfigLinter getColumnConfigLinter() {
        return columnConfigLinter;
    }

    TableNameLinter getTableNameLinter() {
        return tableNameLinter;
    }
}
