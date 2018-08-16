package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import liquibase.change.core.MergeColumnChange;
import liquibase.exception.ChangeLogParseException;

public class MergeColumnChangeLinter implements Linter<MergeColumnChange> {

    private ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(MergeColumnChange change, Rules rules) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectName(change.getFinalColumnName(), change, rules);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
