package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.Linter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import liquibase.change.core.CreateIndexChange;
import liquibase.exception.ChangeLogParseException;

public class CreateIndexChangeLinter implements Linter<CreateIndexChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(CreateIndexChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getIndexName(), change, ruleRunner);
        ruleRunner.forChange(change).run(RuleType.CREATE_INDEX_NAME, change.getIndexName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
