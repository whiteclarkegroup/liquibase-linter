package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class AddForeignKeyConstraintChangeLinter implements Linter<AddForeignKeyConstraintChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddForeignKeyConstraintChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getConstraintName(), change, ruleConfigs);
        RuleRunner.forChange(ruleConfigs, change)
                .run(RuleType.FOREIGN_KEY_MUST_BE_NAMED, change.getConstraintName())
                .run(RuleType.FOREIGN_KEY_MUST_USE_BASE_AND_REFERENCE_TABLE_NAME, change.getConstraintName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
