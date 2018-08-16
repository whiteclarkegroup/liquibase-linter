package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.core.AddUniqueConstraintChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class AddUniqueConstraintChangeLinter implements Linter<AddUniqueConstraintChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddUniqueConstraintChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getConstraintName(), change, ruleConfigs);
        RuleRunner.forChange(ruleConfigs, change).run(RuleType.UNIQUE_CONSTRAINT_NAME, change.getConstraintName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
