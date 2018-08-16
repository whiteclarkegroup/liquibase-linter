package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.core.RenameColumnChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class RenameColumnChangeLinter implements Linter<RenameColumnChange> {

    private ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(RenameColumnChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectName(change.getNewColumnName(), change, ruleConfigs);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
