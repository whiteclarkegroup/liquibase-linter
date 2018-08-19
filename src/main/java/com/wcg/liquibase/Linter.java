package com.wcg.liquibase;

import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.Change;
import liquibase.exception.ChangeLogParseException;

public interface Linter<T extends Change> {

    void lint(T change, RuleRunner ruleRunner) throws ChangeLogParseException;

}
