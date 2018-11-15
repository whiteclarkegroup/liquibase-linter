package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.Linter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import liquibase.change.core.AddUniqueConstraintChange;
import liquibase.exception.ChangeLogParseException;

public class AddUniqueConstraintChangeLinter implements Linter<AddUniqueConstraintChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddUniqueConstraintChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getConstraintName(), change, ruleRunner);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
