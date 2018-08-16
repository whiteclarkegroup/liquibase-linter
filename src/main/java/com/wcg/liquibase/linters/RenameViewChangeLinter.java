package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.core.RenameViewChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class RenameViewChangeLinter implements Linter<RenameViewChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(RenameViewChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getNewViewName(), change, ruleConfigs);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
