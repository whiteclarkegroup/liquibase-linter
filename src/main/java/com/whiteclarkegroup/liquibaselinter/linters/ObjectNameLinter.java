package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.ChangeLogParseExceptionHelper;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import liquibase.change.AbstractChange;
import liquibase.exception.ChangeLogParseException;

public class ObjectNameLinter {

    void lintObjectName(final String objectName, final AbstractChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        ensureNameTruthy(objectName, change);
        ruleRunner.forChange(change).run(RuleType.OBJECT_NAME, objectName);
        lintObjectNameLength(objectName, change, ruleRunner);
    }

    void lintObjectNameLength(final String objectName, final AbstractChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        ensureNameTruthy(objectName, change);
        ruleRunner.forChange(change).run(RuleType.OBJECT_NAME_LENGTH, objectName);
    }

    private void ensureNameTruthy(String objectName, AbstractChange change) throws ChangeLogParseException {
        if (objectName == null) {
            throw ChangeLogParseExceptionHelper.build(null, change, "Object name is null");
        }
    }

}
