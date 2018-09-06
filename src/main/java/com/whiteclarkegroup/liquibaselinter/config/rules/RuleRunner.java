package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.whiteclarkegroup.liquibaselinter.ChangeLogParseExceptionHelper;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
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
                if (shouldApply(ruleType, rule, change) && rule.invalid(object, change)) {
                    String errorMessage = Optional.ofNullable(rule.getErrorMessage()).orElse(ruleType.getDefaultErrorMessage());
                    if (rule instanceof WithFormattedErrorMessage) {
                        errorMessage = ((WithFormattedErrorMessage) rule).formatErrorMessage(errorMessage, object);
                    }
                    throw ChangeLogParseExceptionHelper.build(databaseChangeLog, change, errorMessage);
                }
            }
            return this;
        }

        private boolean shouldApply(RuleType ruleType, Rule rule, Change change) {
            return rule.getRuleConfig().isEnabled() && evaluateCondition(rule, change) && !isIgnored(ruleType);
        }

        private boolean evaluateCondition(Rule rule, Change change) {
            return rule.getRuleConfig().getConditionalExpression()
                    .map(expression -> expression.getValue(change, boolean.class))
                    .orElse(true);
        }

        private boolean isIgnored(RuleType ruleType) {
            if (change == null || change.getChangeSet().getComments() == null || !change.getChangeSet().getComments().contains("lql-ignore:")) {
                return false;
            }
            final String comments = change.getChangeSet().getComments();
            final String toIgnore = comments.substring(comments.indexOf("lql-ignore:"));
            final String[] split = toIgnore.split(",");
            for (String key : split) {
                if (ruleType.getKey().equalsIgnoreCase(key)) {
                    return true;
                }
            }
            return false;
        }

    }

}
