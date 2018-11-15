package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.Linter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.exception.ChangeLogParseException;

public class AddColumnChangeLinter implements Linter<AddColumnChange> {

    private final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddColumnChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        for (AddColumnConfig addColumnConfig : change.getColumns()) {
            getObjectNameLinter().lintObjectName(addColumnConfig.getName(), change, ruleRunner);
        }
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
