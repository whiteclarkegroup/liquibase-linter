package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.core.AddUniqueConstraintChange;
import liquibase.exception.ChangeLogParseException;

public class AddUniqueConstraintChangeLinter implements Linter<AddUniqueConstraintChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddUniqueConstraintChange change, Rules rules) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getConstraintName(), change, rules);
        RuleRunner.forChange(change).run(rules.getUniqueConstraintNameRule(), change.getConstraintName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
