package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import liquibase.change.Change;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.ChangeLogParseException;

class ColumnConfigLinter {

    void lintColumnConfig(ChangeWithColumns<? extends ColumnConfig> change, RuleRunner ruleRunner) throws ChangeLogParseException {
        for (ColumnConfig columnConfig : change.getColumns()) {
            final ConstraintsConfig constraints = columnConfig.getConstraints();
            ruleRunner.forChange((Change) change)
                    .run(RuleType.CREATE_COLUMN_REMARKS, columnConfig.getRemarks())
                    .run(RuleType.CREATE_COLUMN_NULLABLE_CONSTRAINT, constraints)
                    .run(RuleType.CREATE_COLUMN_NO_DEFINE_PRIMARY_KEY, constraints);
        }
    }

}
