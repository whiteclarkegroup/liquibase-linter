package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.whiteclarkegroup.liquibaselinter.ChangeLogParseExceptionHelper;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import liquibase.change.Change;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.*;

public class RuleRunner {
    private final Config config;

    public RuleRunner(Config config) {
        this.config = config;
    }

    }

    public RunningContext forChange(Change change) {
        return new RunningContext(config.getRules(), change, null);
    }

    public RunningContext forDatabaseChangeLog(DatabaseChangeLog databaseChangeLog) {
        return new RunningContext(config.getRules(), null, databaseChangeLog);
    }

    public RunningContext forGeneric() {
        return new RunningContext(config.getRules(), null, null);
    }

    public static class RunningContext {

        private static final String LQL_IGNORE_TOKEN = "lql-ignore:";
        private final Map<String, RuleConfig> ruleConfigs;
        private final Change change;
        private final DatabaseChangeLog databaseChangeLog;

        private RunningContext(Map<String, RuleConfig> ruleConfigs, Change change, DatabaseChangeLog databaseChangeLog) {
            this.ruleConfigs = ruleConfigs;
            this.change = change;
            this.databaseChangeLog = databaseChangeLog;
        }

        @SuppressWarnings("unchecked")
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
            if (change == null || change.getChangeSet().getComments() == null || !change.getChangeSet().getComments().contains(LQL_IGNORE_TOKEN)) {
                return false;
            }
            final String comments = change.getChangeSet().getComments();
            final String toIgnore = comments.substring(comments.indexOf(LQL_IGNORE_TOKEN) + LQL_IGNORE_TOKEN.length());
            final String[] split = toIgnore.split(",");
            return Arrays.stream(split).anyMatch(key -> ruleType.getKey().equalsIgnoreCase(key));
        }

    }

}
