package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.Linter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
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
