package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.exception.ChangeLogParseException;

public class AddPrimaryKeyChangeLinter implements Linter<AddPrimaryKeyChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddPrimaryKeyChange change, Rules rules) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getConstraintName(), change, rules);
        RuleRunner.forChange(change)
                .run(rules.getPrimaryKeyName(), change.getConstraintName())
                .run(rules.getPrimaryKeyMustUseTableName(), change.getConstraintName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
