package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class AddPrimaryKeyChangeLinter implements Linter<AddPrimaryKeyChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddPrimaryKeyChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getConstraintName(), change, ruleConfigs);
        RuleRunner.forChange(ruleConfigs, change)
                .run(RuleType.PRIMARY_KEY_MUST_BE_NAMED, change.getConstraintName())
                .run(RuleType.PRIMARY_KEY_MUST_USE_TABLE_NAME, change.getConstraintName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
