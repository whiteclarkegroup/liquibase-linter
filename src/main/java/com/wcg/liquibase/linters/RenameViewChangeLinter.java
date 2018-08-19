package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.rules.RuleRunner;
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
