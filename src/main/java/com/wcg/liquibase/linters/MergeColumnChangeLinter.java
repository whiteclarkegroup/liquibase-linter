package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.core.MergeColumnChange;
import liquibase.exception.ChangeLogParseException;

public class MergeColumnChangeLinter implements Linter<MergeColumnChange> {

    private final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(MergeColumnChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectName(change.getFinalColumnName(), change, ruleRunner);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
