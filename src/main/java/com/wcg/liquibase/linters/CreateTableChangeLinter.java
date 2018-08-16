package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.core.CreateTableChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class CreateTableChangeLinter implements Linter<CreateTableChange> {

    private ColumnConfigLinter columnConfigLinter = new ColumnConfigLinter();
    private TableNameLinter tableNameLinter = new TableNameLinter();

    @Override
    public void lint(CreateTableChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        RuleRunner.forChange(ruleConfigs, change).run(RuleType.CREATE_TABLE_REMARKS, change.getRemarks());
        getTableNameLinter().lintTableName(change.getTableName(), change, ruleConfigs);
        getColumnConfigLinter().lintColumnConfig(change, ruleConfigs);
    }

    ColumnConfigLinter getColumnConfigLinter() {
        return columnConfigLinter;
    }

    TableNameLinter getTableNameLinter() {
        return tableNameLinter;
    }
}
