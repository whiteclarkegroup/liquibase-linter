package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Rules;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.Change;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.ChangeLogParseException;

class ColumnConfigLinter {

    void lintColumnConfig(ChangeWithColumns<? extends ColumnConfig> change, Rules rules) throws ChangeLogParseException {
        for (ColumnConfig columnConfig : change.getColumns()) {
            final ConstraintsConfig constraints = columnConfig.getConstraints();
            RuleRunner.forChange((Change) change)
                    .run(rules.getCreateColumnRemarks(), columnConfig.getRemarks())
                    .run(rules.getCreateColumnNullableConstraint(), constraints)
                    .run(rules.getCreateColumnNoDefinePrimaryKey(), constraints);
        }
    }

}
