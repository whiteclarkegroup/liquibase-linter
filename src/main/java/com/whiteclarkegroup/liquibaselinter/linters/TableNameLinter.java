package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import liquibase.change.AbstractChange;
import liquibase.exception.ChangeLogParseException;

public class TableNameLinter {

    public void lintTableName(final String tableName, final AbstractChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        ruleRunner.forChange(change)
                .run(RuleType.TABLE_NAME, tableName)
                .run(RuleType.TABLE_NAME_LENGTH, tableName);
    }

}
