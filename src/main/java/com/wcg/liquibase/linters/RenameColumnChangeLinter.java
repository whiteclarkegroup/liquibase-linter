package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import liquibase.change.core.RenameColumnChange;
import liquibase.exception.ChangeLogParseException;

public class RenameColumnChangeLinter implements Linter<RenameColumnChange> {

    private ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(RenameColumnChange change, Rules rules) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectName(change.getNewColumnName(), change, rules);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
