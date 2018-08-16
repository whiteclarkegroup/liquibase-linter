package com.wcg.liquibase;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public interface Linter<T extends Change> {

    void lint(T change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException;

}
