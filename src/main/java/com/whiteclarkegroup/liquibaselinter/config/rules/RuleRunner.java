package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.whiteclarkegroup.liquibaselinter.ChangeLogParseExceptionHelper;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import liquibase.change.Change;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.servicelocator.DefaultPackageScanClassResolver;
import liquibase.servicelocator.PackageScanClassResolver;

import java.util.*;
import java.util.stream.Collectors;

public class RuleRunner {
    private static final String CORE_RULES_PACKAGE = "com/whiteclarkegroup/liquibaselinter/config/rules/core";
    private final PackageScanClassResolver packageScanner = new DefaultPackageScanClassResolver();

    private final Config config;
    private final List<ChangeRule> changeRules;

    public RuleRunner(Config config) {
        this.config = config;
        this.changeRules = discoverChangeRules();
    }

    private List<ChangeRule> discoverChangeRules() {
        return packageScanner.findImplementations(ChangeRule.class, CORE_RULES_PACKAGE).stream()
            .map(found -> {
                try {
                    Class<? extends ChangeRule> clazz = (Class<? extends ChangeRule>) found;
                    return clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .filter(changeRule -> config.isRuleEnabled(changeRule.getName()))
            .map(changeRule -> changeRule.configure(config.getRules().get(changeRule.getName())))
            .collect(Collectors.toList());
    }

    public RunningContext forChange(Change change) {
        return new RunningContext(config.getRules(), changeRules, change, null);
    }

    public RunningContext forDatabaseChangeLog(DatabaseChangeLog databaseChangeLog) {
        return new RunningContext(config.getRules(), null, null, databaseChangeLog);
    }

    public RunningContext forGeneric() {
        return new RunningContext(config.getRules(), null, null, null);
    }

    public static class RunningContext {

        private static final String LQL_IGNORE_TOKEN = "lql-ignore:";
        private final Map<String, RuleConfig> ruleConfigs;
        private final List<ChangeRule> changeRules;
        private final Change change;
        private final DatabaseChangeLog databaseChangeLog;

        private RunningContext(Map<String, RuleConfig> ruleConfigs, List<ChangeRule> changeRules, Change change, DatabaseChangeLog databaseChangeLog) {
            this.ruleConfigs = ruleConfigs;
            this.changeRules = changeRules;
            this.change = change;
            this.databaseChangeLog = databaseChangeLog;
        }

        public RunningContext checkChange() throws ChangeLogParseException {
            for (ChangeRule changeRule : changeRules) {
                checkChangeRule(changeRule);
            }
            return this;
        }

        private void checkChangeRule(ChangeRule changeRule) throws ChangeLogParseException {
            if (change.getClass().isAssignableFrom(changeRule.getChangeType())
                && changeRule.supports(change)
                && changeRule.invalid(change)
                && shouldApply(changeRule)) {
                throw ChangeLogParseExceptionHelper.build(databaseChangeLog, change, changeRule.getMessage(change));
            }
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

        private boolean shouldApply(ChangeRule changeRule) {
            return evaluateCondition(changeRule.getConfig(), change) && !isIgnored(changeRule.getName());
        }

        private boolean shouldApply(RuleType ruleType, Rule rule, Change change) {
            return rule.getRuleConfig().isEnabled() && evaluateCondition(rule.getRuleConfig(), change) && !isIgnored(ruleType.getKey());
        }

        private boolean evaluateCondition(RuleConfig ruleConfig, Change change) {
            return ruleConfig.getConditionalExpression()
                    .map(expression -> expression.getValue(change, boolean.class))
                    .orElse(true);
        }

        private boolean isIgnored(String ruleName) {
            if (change == null || change.getChangeSet().getComments() == null || !change.getChangeSet().getComments().contains(LQL_IGNORE_TOKEN)) {
                return false;
            }
            final String comments = change.getChangeSet().getComments();
            final String toIgnore = comments.substring(comments.indexOf(LQL_IGNORE_TOKEN) + LQL_IGNORE_TOKEN.length());
            final String[] split = toIgnore.split(",");
            return Arrays.stream(split).anyMatch(ruleName::equalsIgnoreCase);
        }
    }

}
