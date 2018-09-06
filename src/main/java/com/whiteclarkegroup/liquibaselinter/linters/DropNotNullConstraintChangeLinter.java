package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.Linter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import liquibase.change.core.DropNotNullConstraintChange;
import liquibase.exception.ChangeLogParseException;

public class DropNotNullConstraintChangeLinter implements Linter<DropNotNullConstraintChange> {

    @Override
    public void lint(DropNotNullConstraintChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        ruleRunner.forChange(change)
                .run(RuleType.DROP_NOT_NULL_REQUIRE_COLUMN_DATA_TYPE, change.getColumnDataType());
    }

}
