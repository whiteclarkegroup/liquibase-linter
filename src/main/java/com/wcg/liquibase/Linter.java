package com.wcg.liquibase;

import com.wcg.liquibase.config.Rules;
import liquibase.change.Change;
import liquibase.exception.ChangeLogParseException;

public interface Linter<T extends Change> {

    void lint(T change, Rules rules) throws ChangeLogParseException;

}
