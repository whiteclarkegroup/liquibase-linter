package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.google.common.collect.Streams;
import com.whiteclarkegroup.liquibaselinter.ChangeLogParseExceptionHelper;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class RuleRunner {

    private static final String LQL_IGNORE_TOKEN = "lql-ignore:";
    private static final ServiceLoader<ChangeRule> changeRuleServiceLoader = ServiceLoader.load(ChangeRule.class);
    private static final ServiceLoader<ChangeLogRule> changeLogRuleServiceLoader = ServiceLoader.load(ChangeLogRule.class);
    private static final ServiceLoader<ChangeSetRule> changeSetRuleServiceLoader = ServiceLoader.load(ChangeSetRule.class);

    private final Config config;
    private final List<ChangeRule> changeRules;
    private final List<ChangeSetRule> changeSetRules;
    private final List<ChangeLogRule> changeLogRules;
    private final Report report = new Report();
    private final Set<String> filesParsed;

    public RuleRunner(Config config, Set<String> filesParsed) {
        this.config = config;
        this.filesParsed = filesParsed;
        this.changeRules = assembleRules(changeRuleServiceLoader);
        this.changeSetRules = assembleRules(changeSetRuleServiceLoader);
        this.changeLogRules = assembleRules(changeLogRuleServiceLoader);
    }

    private <T extends LintRule> List<T> assembleRules(ServiceLoader<T> ruleServiceLoader) {
        return Streams.stream(ruleServiceLoader).filter(this::filterRule).collect(Collectors.toList());
    }

    private boolean filterRule(LintRule rule) {
        return rule != null && config.isRuleEnabled(rule.getName());
    }

    public Report getReport() {
        return report;
    }

    public Set<String> getFilesParsed() {
        return filesParsed;
    }

    public void checkChange(Change change) throws ChangeLogParseException {
        for (ChangeRule changeRule : changeRules) {
            if (changeRule.getChangeType().isAssignableFrom(change.getClass()) && changeRule.supports(change)) {
                final List<RuleConfig> configs = config.forRule(changeRule.getName());
                for (RuleConfig ruleConfig : configs) {
                    changeRule.configure(ruleConfig);
                    if (ConditionEvaluator.evaluateCondition(ruleConfig, change) && changeRule.invalid(change)) {
                        handleViolation(changeRule.getMessage(change), changeRule.getName(), ruleConfig, change.getChangeSet().getChangeLog(), change.getChangeSet());
                    }
                }
            }
        }
    }

    public void checkChangeSet(ChangeSet changeSet) throws ChangeLogParseException {
        for (ChangeSetRule changeSetRule : changeSetRules) {
            final List<RuleConfig> configs = config.forRule(changeSetRule.getName());
            for (RuleConfig ruleConfig : configs) {
                changeSetRule.configure(ruleConfig);
                if (ConditionEvaluator.evaluateCondition(ruleConfig, changeSet) && changeSetRule.invalid(changeSet)) {
                    handleViolation(changeSetRule.getMessage(changeSet), changeSetRule.getName(), ruleConfig, changeSet.getChangeLog(), changeSet);
                }
            }
        }
    }

    public void checkChangeLog(DatabaseChangeLog databaseChangeLog) throws ChangeLogParseException {
        for (ChangeLogRule changeLogRule : changeLogRules) {
            final List<RuleConfig> configs = config.forRule(changeLogRule.getName());
            for (RuleConfig ruleConfig : configs) {
                changeLogRule.configure(ruleConfig);
                if (ConditionEvaluator.evaluateCondition(ruleConfig, databaseChangeLog) && changeLogRule.invalid(databaseChangeLog)) {
                    handleViolation(changeLogRule.getMessage(databaseChangeLog), changeLogRule.getName(), ruleConfig, databaseChangeLog, null);
                }
            }
        }
    }

    private void handleViolation(String errorMessage, String rule, RuleConfig ruleConfig, DatabaseChangeLog databaseChangeLog, ChangeSet changeSet) throws ChangeLogParseException {
        if (!isEnabledAfter(ruleConfig) || isIgnored(rule, changeSet)) {
            report.addIgnored(databaseChangeLog, changeSet, rule, errorMessage);
            return;
        }
        if (config.isFailFast()) {
            throw ChangeLogParseExceptionHelper.build(databaseChangeLog, changeSet, errorMessage);
        } else {
            report.addError(databaseChangeLog, changeSet, rule, errorMessage);
        }
    }

    private boolean isIgnored(String ruleName, ChangeSet changeSet) {
        if (changeSet == null || changeSet.getComments() == null || !changeSet.getComments().contains(LQL_IGNORE_TOKEN)) {
            return false;
        }
        final String comments = changeSet.getComments();
        final String toIgnore = comments.substring(comments.indexOf(LQL_IGNORE_TOKEN) + LQL_IGNORE_TOKEN.length());
        final String[] split = toIgnore.split(",");
        return Arrays.stream(split).anyMatch(ruleName::equalsIgnoreCase);
    }

    private boolean isEnabledAfter(RuleConfig ruleConfig) {
        if (!config.isEnabledAfter() && !ruleConfig.isEnabledAfter()) {
            return true;
        }
        if (ruleConfig.isEnabledAfter()) {
            return filesParsed.contains(ruleConfig.getEnableAfter());
        } else {
            return filesParsed.contains(config.getEnableAfter());
        }
    }

}
