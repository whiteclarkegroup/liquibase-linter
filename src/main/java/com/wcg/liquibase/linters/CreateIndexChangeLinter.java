package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.core.CreateIndexChange;
import liquibase.exception.ChangeLogParseException;

public class CreateIndexChangeLinter implements Linter<CreateIndexChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(CreateIndexChange change, Rules rules) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getIndexName(), change, rules);
        RuleRunner.forChange(change).run(rules.getCreateIndexName(), change.getIndexName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
