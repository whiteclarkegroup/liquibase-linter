package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.core.AbstractModifyDataChange;
import liquibase.exception.ChangeLogParseException;

public class ModifyDataChangeLinter implements Linter<AbstractModifyDataChange> {

    @Override
    public void lint(AbstractModifyDataChange change, Rules rules) throws ChangeLogParseException {
        RuleRunner.forChange(change)
                .run(rules.getModifyDataEnforceWhere(), change)
                .run(rules.getModifyDataStartsWithWhere(), change);
    }
}
