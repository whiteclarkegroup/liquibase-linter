package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.Linter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import liquibase.change.core.RenameViewChange;
import liquibase.exception.ChangeLogParseException;

public class RenameViewChangeLinter implements Linter<RenameViewChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(RenameViewChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getNewViewName(), change, ruleRunner);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
