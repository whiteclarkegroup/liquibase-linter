package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.ChangeLogParseExceptionHelper;
import liquibase.change.Change;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;
import java.util.Optional;

public class RuleRunner {

    private final Map<String, RuleConfig> ruleConfigs;

    public RuleRunner(Map<String, RuleConfig> ruleConfigs) {
        this.ruleConfigs = ruleConfigs;
    }

    public RunningContext forChange(Change change) {
        return new RunningContext(this.ruleConfigs, change, null);
    }

    public RunningContext forDatabaseChangeLog(DatabaseChangeLog databaseChangeLog) {
        return new RunningContext(ruleConfigs, null, databaseChangeLog);
    }

    public RunningContext forGeneric() {
        return new RunningContext(ruleConfigs, null, null);
    }

    public static class RunningContext {

        private final Map<String, RuleConfig> ruleConfigs;
        private final Change change;
        private final DatabaseChangeLog databaseChangeLog;

        private RunningContext(Map<String, RuleConfig> ruleConfigs, Change change, DatabaseChangeLog databaseChangeLog) {
            this.ruleConfigs = ruleConfigs;
            this.change = change;
            this.databaseChangeLog = databaseChangeLog;
        }

        public RunningContext run(RuleType ruleType, Object object) throws ChangeLogParseException {
            final Optional<Rule> optionalRule = ruleType.create(ruleConfigs);
            if (optionalRule.isPresent()) {
                Rule rule = optionalRule.get();
                if (shouldApply(rule, change) && rule.invalid(object, change)) {
                    String errorMessage = Optional.ofNullable(rule.getErrorMessage()).orElse(ruleType.getDefaultErrorMessage());
                    if (rule instanceof WithFormattedErrorMessage) {
                        errorMessage = ((WithFormattedErrorMessage) rule).formatErrorMessage(errorMessage, object);
                    }
                    throw ChangeLogParseExceptionHelper.build(databaseChangeLog, change, errorMessage);
                }
            }
            return this;
        }

        private boolean shouldApply(Rule rule, Change change) {
            return rule.getRuleConfig().isEnabled() && evaluateCondition(rule, change);
        }

        private boolean evaluateCondition(Rule rule, Change change) {
            return rule.getRuleConfig().getConditionalExpression()
                    .map(expression -> expression.getValue(change, boolean.class))
                    .orElse(true);
        }

    }

}
