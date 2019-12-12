package com.whiteclarkegroup.liquibaselinter.config.rules;

import liquibase.Contexts;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;

import java.util.Optional;

public class ConditionHelper {

    private ConditionHelper() {

    }

    public static boolean evaluateCondition(RuleConfig ruleConfig, Change change) {
        return ruleConfig.getConditionalExpression()
            .map(expression -> expression.getValue(new ConditionContext(change.getChangeSet().getChangeLog(), change.getChangeSet(), change), boolean.class))
            .orElse(true);
    }

    public static boolean evaluateCondition(RuleConfig ruleConfig, ChangeSet changeSet) {
        return ruleConfig.getConditionalExpression()
            .map(expression -> expression.getValue(new ConditionContext(changeSet.getChangeLog(), changeSet, null), boolean.class))
            .orElse(true);
    }

    public static boolean evaluateCondition(RuleConfig ruleConfig, DatabaseChangeLog databaseChangeLog) {
        return ruleConfig.getConditionalExpression()
            .map(expression -> expression.getValue(new ConditionContext(databaseChangeLog, null, null), boolean.class))
            .orElse(true);
    }

    private static class ConditionContext {
        private final DatabaseChangeLog changeLog;
        private final ChangeSet changeSet;
        private final Change change;

        private ConditionContext(DatabaseChangeLog changeLog, ChangeSet changeSet, Change change) {
            this.changeLog = changeLog;
            this.changeSet = changeSet;
            this.change = change;
        }

        public DatabaseChangeLog getChangeLog() {
            return changeLog;
        }

        public ChangeSet getChangeSet() {
            return changeSet;
        }

        public Change getChange() {
            return change;
        }

        public boolean matchesContext(String... toMatch) {
            return Optional.ofNullable(changeSet)
                .map(ChangeSet::getContexts)
                .map(contexts -> !contexts.isEmpty() && contexts.matches(new Contexts(toMatch)))
                .orElse(false);
        }
    }

}
