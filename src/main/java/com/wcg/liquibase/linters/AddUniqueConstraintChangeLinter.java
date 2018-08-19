package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.core.AddUniqueConstraintChange;
import liquibase.exception.ChangeLogParseException;

public class AddUniqueConstraintChangeLinter implements Linter<AddUniqueConstraintChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddUniqueConstraintChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getConstraintName(), change, ruleRunner);
        ruleRunner.forChange(change).run(RuleType.UNIQUE_CONSTRAINT_NAME, change.getConstraintName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
