package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.exception.ChangeLogParseException;

public class AddForeignKeyConstraintChangeLinter implements Linter<AddForeignKeyConstraintChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddForeignKeyConstraintChange change, Rules rules) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getConstraintName(), change, rules);
        RuleRunner.forChange(change)
                .run(rules.getForeignKeyName(), change.getConstraintName())
                .run(rules.getForeignKeyTableAndReferencedTableNameRule(), change.getConstraintName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
