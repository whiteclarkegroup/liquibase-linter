package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.Linter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.exception.ChangeLogParseException;

public class AddForeignKeyConstraintChangeLinter implements Linter<AddForeignKeyConstraintChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddForeignKeyConstraintChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getConstraintName(), change, ruleRunner);
        ruleRunner.forChange(change)
                .run(RuleType.FOREIGN_KEY_NAME, change.getConstraintName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
