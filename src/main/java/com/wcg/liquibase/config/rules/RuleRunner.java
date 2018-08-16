package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.ChangeLogParseExceptionHelper;
import liquibase.change.Change;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.function.Supplier;

public class RuleRunner {

    private final Change change;
    private final DatabaseChangeLog databaseChangeLog;

    private RuleRunner(Change change, DatabaseChangeLog databaseChangeLog) {
        this.change = change;
        this.databaseChangeLog = databaseChangeLog;
    }

    public static RuleRunner forChange(final Change change) {
        return new RuleRunner(change, null);
    }

    public static RuleRunner forDatabaseChangeLog(final DatabaseChangeLog databaseChangeLog) {
        return new RuleRunner(null, databaseChangeLog);
    }

    public static RuleRunner forGeneric() {
        return new RuleRunner(null, null);
    }

    public RuleRunner run(Rule rule, Supplier<Object> objectSupplier) throws ChangeLogParseException {
        run(rule, objectSupplier.get());
        return this;
    }

    public RuleRunner run(Rule rule, Object object) throws ChangeLogParseException {
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
