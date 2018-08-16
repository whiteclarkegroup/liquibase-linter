package com.wcg.liquibase.linters;

import com.wcg.liquibase.ChangeLogParseExceptionHelper;
import com.wcg.liquibase.config.Rules;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.AbstractChange;
import liquibase.exception.ChangeLogParseException;

public class ObjectNameLinter {

    void lintObjectName(final String objectName, final AbstractChange change, final Rules rules) throws ChangeLogParseException {
        ensureNameTruthy(objectName, change);
        RuleRunner.forChange(change).run(rules.getObjectName(), objectName);
        lintObjectNameLength(objectName, change, rules);
    }

    void lintObjectNameLength(final String objectName, final AbstractChange change, Rules rules) throws ChangeLogParseException {
        ensureNameTruthy(objectName, change);
        RuleRunner.forChange(change).run(rules.getObjectNameLength(), objectName);
    }

    private void ensureNameTruthy(String objectName, AbstractChange change) throws ChangeLogParseException {
        if (objectName == null) {
            throw ChangeLogParseExceptionHelper.build(null, change, "Object name is null");
        }
    }

}
