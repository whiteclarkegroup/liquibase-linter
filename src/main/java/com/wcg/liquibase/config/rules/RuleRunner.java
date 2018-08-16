package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.ChangeLogParseExceptionHelper;
import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;
import java.util.function.Supplier;

public class RuleRunner {

    private final Map<String, RuleConfig> ruleConfigs;
    private final Change change;
    private final DatabaseChangeLog databaseChangeLog;

    private RuleRunner(Map<String, RuleConfig> ruleConfigs, Change change, DatabaseChangeLog databaseChangeLog) {
        this.ruleConfigs = ruleConfigs;
        this.change = change;
        this.databaseChangeLog = databaseChangeLog;
    }

    public static RuleRunner forChange(final Map<String, RuleConfig> ruleConfigs, final Change change) {
        return new RuleRunner(ruleConfigs, change, null);
    }

    public static RuleRunner forDatabaseChangeLog(final Map<String, RuleConfig> ruleConfigs, final DatabaseChangeLog databaseChangeLog) {
        return new RuleRunner(ruleConfigs, null, databaseChangeLog);
    }

    public static RuleRunner forGeneric(final Map<String, RuleConfig> ruleConfigs) {
        return new RuleRunner(ruleConfigs, null, null);
    }

    public RuleRunner run(RuleType ruleType, Object object) throws ChangeLogParseException {
        final Rule rule = ruleType.create(ruleConfigs);
        if (shouldApply(rule, change) && rule.invalid(object, change)) {
            throw ChangeLogParseExceptionHelper.build(databaseChangeLog, change, rule.buildErrorMessage(object, change));
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
