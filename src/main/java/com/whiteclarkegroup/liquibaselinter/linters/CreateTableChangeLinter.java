package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.Linter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import liquibase.change.core.CreateTableChange;
import liquibase.exception.ChangeLogParseException;

public class CreateTableChangeLinter implements Linter<CreateTableChange> {

    private final ColumnConfigLinter columnConfigLinter = new ColumnConfigLinter();
    private final TableNameLinter tableNameLinter = new TableNameLinter();

    @Override
    public void lint(CreateTableChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        ruleRunner.forChange(change).run(RuleType.CREATE_TABLE_REMARKS, change.getRemarks());
        getTableNameLinter().lintTableName(change.getTableName(), change, ruleRunner);
        getColumnConfigLinter().lintColumnConfig(change, ruleRunner);
    }

    ColumnConfigLinter getColumnConfigLinter() {
        return columnConfigLinter;
    }

    TableNameLinter getTableNameLinter() {
        return tableNameLinter;
    }
}
