package com.wcg.liquibase.linters;

import com.wcg.liquibase.ChangeLogParseExceptionHelper;
import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.AbstractChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class ObjectNameLinter {

    void lintObjectName(final String objectName, final AbstractChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        ensureNameTruthy(objectName, change);
        RuleRunner.forChange(ruleConfigs, change).run(RuleType.OBJECT_NAME, objectName);
        lintObjectNameLength(objectName, change, ruleConfigs);
    }

    void lintObjectNameLength(final String objectName, final AbstractChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        ensureNameTruthy(objectName, change);
        RuleRunner.forChange(ruleConfigs, change).run(RuleType.OBJECT_NAME_LENGTH, objectName);
    }

    private void ensureNameTruthy(String objectName, AbstractChange change) throws ChangeLogParseException {
        if (objectName == null) {
            throw ChangeLogParseExceptionHelper.build(null, change, "Object name is null");
        }
    }

}
