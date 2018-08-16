package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import liquibase.change.core.RenameViewChange;
import liquibase.exception.ChangeLogParseException;

public class RenameViewChangeLinter implements Linter<RenameViewChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(RenameViewChange change, Rules rules) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getNewViewName(), change, rules);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
