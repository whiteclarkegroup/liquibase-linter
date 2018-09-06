package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.Linter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import liquibase.change.core.RenameColumnChange;
import liquibase.exception.ChangeLogParseException;

public class RenameColumnChangeLinter implements Linter<RenameColumnChange> {

    private final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(RenameColumnChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectName(change.getNewColumnName(), change, ruleRunner);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
