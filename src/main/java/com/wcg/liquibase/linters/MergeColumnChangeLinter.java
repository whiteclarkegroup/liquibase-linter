package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.core.MergeColumnChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class MergeColumnChangeLinter implements Linter<MergeColumnChange> {

    private ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(MergeColumnChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectName(change.getFinalColumnName(), change, ruleConfigs);
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
