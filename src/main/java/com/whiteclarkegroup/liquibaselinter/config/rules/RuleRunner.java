package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.google.common.collect.Streams;
import com.whiteclarkegroup.liquibaselinter.ChangeLogParseExceptionHelper;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.*;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class RuleRunner {

    private static final String LQL_IGNORE_TOKEN = "lql-ignore:";

    private final Config config;
    private final List<ChangeRule> changeRules = Streams.stream(ServiceLoader.load(ChangeRule.class)).collect(toList());
    private final List<ChangeSetRule> changeSetRules = Streams.stream(ServiceLoader.load(ChangeSetRule.class)).collect(toList());
    private final List<ChangeLogRule> changeLogRules = Streams.stream(ServiceLoader.load(ChangeLogRule.class)).collect(toList());
    private final Report report = new Report();
    private final Set<String> filesParsed;

    public RuleRunner(Config config, Set<String> filesParsed) {
        this.config = config;
        this.filesParsed = filesParsed;
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
                final String ruleName = changeRule.getName();
                final List<RuleConfig> configs = config.forRule(ruleName);

                for (RuleConfig ruleConfig : configs) {
                    if (ruleConfig.isEnabled()) {
                        changeRule.configure(ruleConfig);
                        final ChangeSet changeSet = change.getChangeSet();
                        final DatabaseChangeLog databaseChangeLog = changeSet.getChangeLog();
                        final String message = changeRule.getMessage(change);

                        if (ConditionHelper.evaluateCondition(ruleConfig, change) && changeRule.invalid(change)) {
                            handleViolation(databaseChangeLog, changeSet, ruleName, ruleConfig, message);
                        } else {
                            report.addPassed(databaseChangeLog, changeSet, ruleName, message);
                        }
                    }
                }
            }
        }
    }

    public void checkChangeSet(ChangeSet changeSet) throws ChangeLogParseException {
        for (ChangeSetRule changeSetRule : changeSetRules) {
            final String ruleName = changeSetRule.getName();
            final List<RuleConfig> configs = config.forRule(ruleName);
            for (RuleConfig ruleConfig : configs) {
                if (ruleConfig.isEnabled()) {
                    changeSetRule.configure(ruleConfig);
                    final DatabaseChangeLog databaseChangeLog = changeSet.getChangeLog();
                    final String message = changeSetRule.getMessage(changeSet);
                    if (ConditionHelper.evaluateCondition(ruleConfig, changeSet) && changeSetRule.invalid(changeSet)) {
                        handleViolation(databaseChangeLog, changeSet, ruleName, ruleConfig, message);
                    } else {
                        report.addPassed(databaseChangeLog, changeSet, ruleName, message);
                    }
                }
            }
        }
    }

    public void checkChangeLog(DatabaseChangeLog databaseChangeLog) throws ChangeLogParseException {
        for (ChangeLogRule changeLogRule : changeLogRules) {
            final String ruleName = changeLogRule.getName();
            final List<RuleConfig> configs = config.forRule(ruleName);
            for (RuleConfig ruleConfig : configs) {
                if (ruleConfig.isEnabled()) {
                    changeLogRule.configure(ruleConfig);
                    final String message = changeLogRule.getMessage(databaseChangeLog);
                    if (ConditionHelper.evaluateCondition(ruleConfig, databaseChangeLog) && changeLogRule.invalid(databaseChangeLog)) {
                        handleViolation(databaseChangeLog, null, ruleName, ruleConfig, message);
                    } else {
                        report.addPassed(databaseChangeLog, null, ruleName, message);
                    }
                }
            }
        }
    }

    private void handleViolation(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, RuleConfig ruleConfig, String message) throws ChangeLogParseException {
        if (isSkipped(ruleConfig)) {
            report.addSkipped(databaseChangeLog, changeSet, rule, message);
        } else if (isIgnored(rule, changeSet)) {
            report.addIgnored(databaseChangeLog, changeSet, rule, message);
        } else if (config.isFailFast()) {
            throw ChangeLogParseExceptionHelper.build(databaseChangeLog, changeSet, message);
        } else {
            report.addError(databaseChangeLog, changeSet, rule, message);
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

    private boolean isSkipped(RuleConfig ruleConfig) {
        if (config.isEnabledAfter() && !filesParsed.contains(config.getEnableAfter())) {
            return true;
        } else if (ruleConfig.isEnabledAfter() && !filesParsed.contains(ruleConfig.getEnableAfter())) {
            return true;
        }
        return false;
    }

    public int countDisabledRules() {
        return countDisabledChangeLogRules() + countDisabledChangeSetRules() + countDisabledChangeRules();
    }

    public int countDisabledChangeLogRules() {
        int count = 0;
        for (ChangeLogRule rule : changeLogRules) {
            final String ruleName = rule.getName();
            final List<RuleConfig> configs = config.forRule(ruleName);
            for (RuleConfig config : configs) {
                if (!config.isEnabled()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int countDisabledChangeSetRules() {
        int count = 0;
        for (ChangeSetRule rule : changeSetRules) {
            final String ruleName = rule.getName();
            final List<RuleConfig> configs = config.forRule(ruleName);
            for (RuleConfig config : configs) {
                if (!config.isEnabled()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int countDisabledChangeRules() {
        int count = 0;
        for (ChangeRule<Change> rule : changeRules) {
            final String ruleName = rule.getName();
            final List<RuleConfig> configs = config.forRule(ruleName);
            for (RuleConfig config : configs) {
                if (!config.isEnabled()) {
                    count++;
                }
            }
        }
        return count;
    }
}
