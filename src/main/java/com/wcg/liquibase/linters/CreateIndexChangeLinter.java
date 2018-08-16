package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.core.CreateIndexChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class CreateIndexChangeLinter implements Linter<CreateIndexChange> {

    private static final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(CreateIndexChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        getObjectNameLinter().lintObjectNameLength(change.getIndexName(), change, ruleConfigs);
        RuleRunner.forChange(ruleConfigs, change).run(RuleType.CREATE_INDEX_NAME, change.getIndexName());
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }

}
