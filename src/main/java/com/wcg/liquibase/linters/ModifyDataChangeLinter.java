package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.core.AbstractModifyDataChange;
import liquibase.exception.ChangeLogParseException;

public class ModifyDataChangeLinter implements Linter<AbstractModifyDataChange> {

    @Override
    public void lint(AbstractModifyDataChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        ruleRunner.forChange(change)
                .run(RuleType.MODIFY_DATA_ENFORCE_WHERE, change)
                .run(RuleType.MODIFY_DATA_STARTS_WITH_WHERE, change);
    }
}
