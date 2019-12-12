package com.whiteclarkegroup.liquibaselinter.config.rules;

import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;

public class ConditionEvaluator {

    public static boolean evaluateCondition(RuleConfig ruleConfig, Change change) {
        return ruleConfig.getConditionalExpression()
            .map(expression -> expression.getValue(change, boolean.class))
            .orElse(true);
    }

    public static boolean evaluateCondition(RuleConfig ruleConfig, ChangeSet changeSet) {
        return ruleConfig.getConditionalExpression()
            .map(expression -> expression.getValue(changeSet, boolean.class))
            .orElse(true);
    }

    public static boolean evaluateCondition(RuleConfig ruleConfig, DatabaseChangeLog databaseChangeLog) {
        return ruleConfig.getConditionalExpression()
            .map(expression -> expression.getValue(databaseChangeLog, boolean.class))
            .orElse(true);
    }

}
