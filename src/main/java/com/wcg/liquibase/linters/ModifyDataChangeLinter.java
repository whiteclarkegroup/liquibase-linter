package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.core.AbstractModifyDataChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class ModifyDataChangeLinter implements Linter<AbstractModifyDataChange> {

    @Override
    public void lint(AbstractModifyDataChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        RuleRunner.forChange(ruleConfigs, change)
                .run(RuleType.MODIFY_DATA_ENFORCE_WHERE, change)
                .run(RuleType.MODIFY_DATA_STARTS_WITH_WHERE, change);
    }
}
