package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.AbstractChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class TableNameLinter {

    public void lintTableName(final String tableName, final AbstractChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        RuleRunner.forChange(ruleConfigs, change)
                .run(RuleType.TABLE_NAME, tableName)
                .run(RuleType.TABLE_NAME_LENGTH, tableName);
    }

}
