package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.core.RenameTableChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class RenameTableChangeLinter implements Linter<RenameTableChange> {

    private TableNameLinter tableNameLinter = new TableNameLinter();

    @Override
    public void lint(RenameTableChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        getTableNameLinter().lintTableName(change.getNewTableName(), change, ruleConfigs);
    }

    TableNameLinter getTableNameLinter() {
        return tableNameLinter;
    }

}
