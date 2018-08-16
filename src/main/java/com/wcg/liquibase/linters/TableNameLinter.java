package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Rules;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.AbstractChange;
import liquibase.exception.ChangeLogParseException;

public class TableNameLinter {

    public void lintTableName(final String tableName, final AbstractChange change, final Rules rules) throws ChangeLogParseException {
        RuleRunner.forChange(change)
                .run(rules.getTableName(), tableName)
                .run(rules.getTableNameLength(), tableName);
    }

}
