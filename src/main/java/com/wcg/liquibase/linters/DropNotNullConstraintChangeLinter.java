package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.core.DropNotNullConstraintChange;
import liquibase.exception.ChangeLogParseException;

public class DropNotNullConstraintChangeLinter implements Linter<DropNotNullConstraintChange> {

    @Override
    public void lint(DropNotNullConstraintChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        ruleRunner.forChange(change)
                .run(RuleType.DROP_NOT_NULL_REQUIRE_COLUMN_DATA_TYPE, change.getColumnDataType());
    }

}
