package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.google.common.collect.Streams;
import com.whiteclarkegroup.liquibaselinter.ChangeLogParseExceptionHelper;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.ReportItem;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.*;
import java.util.stream.Collectors;

public class RuleRunner {

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
        this.changeRules = assembleChangeRules();
        this.changeSetRules = assembleChangeSetRules();
        this.changeLogRules = assembleChangeLogRules();
    }

    private List<ChangeRule> assembleChangeRules() {
        return Streams.stream(changeRuleServiceLoader).filter(this::filterRule).collect(Collectors.toList());
    }

    private List<ChangeSetRule> assembleChangeSetRules() {
        return Streams.stream(changeSetRuleServiceLoader).filter(this::filterRule).collect(Collectors.toList());
    }

    private List<ChangeLogRule> assembleChangeLogRules() {
        return Streams.stream(changeLogRuleServiceLoader).filter(this::filterRule).collect(Collectors.toList());
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

    public RunningContext forChange(Change change) {
        return new RunningContext(config, changeRules, null, changeLogRules, change, change.getChangeSet().getChangeLog(), report.getReportItems(), change.getChangeSet(), filesParsed);
    }

    public RunningContext forDatabaseChangeLog(DatabaseChangeLog databaseChangeLog) {
        return new RunningContext(config, null, null, changeLogRules, null, databaseChangeLog, report.getReportItems(), null, filesParsed);
    }

    public RunningContext forChangeSet(ChangeSet changeSet) {
        return new RunningContext(config, null, changeSetRules, null, null, changeSet.getChangeLog(), report.getReportItems(), changeSet, filesParsed);
    }

    @SuppressWarnings("unchecked")
    public static class RunningContext {

        private static final String LQL_IGNORE_TOKEN = "lql-ignore:";
        private final Config config;
        private final List<ChangeRule> changeRules;
        private final List<ChangeSetRule> changeSetRules;
        private final List<ChangeLogRule> changeLogRules;
        private final Change change;
        private final DatabaseChangeLog databaseChangeLog;
        private final Collection<ReportItem> reportItems;
        private final ChangeSet changeSet;
        private final Set<String> filesParsed;

        private RunningContext(Config config,
                               List<ChangeRule> changeRules,
                               List<ChangeSetRule> changeSetRules,
                               List<ChangeLogRule> changeLogRules,
                               Change change,
                               DatabaseChangeLog databaseChangeLog,
                               Collection<ReportItem> reportItems,
                               ChangeSet changeSet,
                               Set<String> filesParsed) {
            this.config = config;
            this.changeRules = changeRules;
            this.changeSetRules = changeSetRules;
            this.changeLogRules = changeLogRules;
            this.change = change;
            this.databaseChangeLog = databaseChangeLog;
            this.reportItems = reportItems;
            this.changeSet = changeSet;
            this.filesParsed = filesParsed;
        }

        public RunningContext checkChange() throws ChangeLogParseException {
            for (ChangeRule changeRule : changeRules) {
                checkChangeRule(changeRule);
            }
            return this;
        }

        private void checkChangeRule(ChangeRule changeRule) throws ChangeLogParseException {
            if (changeRule.getChangeType().isAssignableFrom(change.getClass()) && changeRule.supports(change)) {
                final List<RuleConfig> configs = config.forRule(changeRule.getName());
                for (RuleConfig ruleConfig : configs) {
                    changeRule.configure(ruleConfig);
                    if (evaluateCondition(ruleConfig, change) && changeRule.invalid(change)) {
                        handleViolation(changeRule.getMessage(change), changeRule.getName(), ruleConfig);
                    }
                }
            }
        }

        public RunningContext checkChangeSet() throws ChangeLogParseException {
            for (ChangeSetRule changeSetRule : changeSetRules) {
                checkChangeSetRule(changeSetRule);
            }
            return this;
        }

        private void checkChangeSetRule(ChangeSetRule changeSetRule) throws ChangeLogParseException {
            final List<RuleConfig> configs = config.forRule(changeSetRule.getName());
            for (RuleConfig ruleConfig : configs) {
                changeSetRule.configure(ruleConfig);
                if (evaluateCondition(ruleConfig, change) && changeSetRule.invalid(changeSet)) {
                    handleViolation(changeSetRule.getMessage(changeSet), changeSetRule.getName(), ruleConfig);
                }
            }
        }

        public RunningContext checkChangeLog() throws ChangeLogParseException {
            for (ChangeLogRule changeLogRule : changeLogRules) {
                checkChangeLogRule(changeLogRule);
            }
            return this;
        }

        private void checkChangeLogRule(ChangeLogRule changeLogRule) throws ChangeLogParseException {
            final List<RuleConfig> configs = config.forRule(changeLogRule.getName());
            for (RuleConfig ruleConfig : configs) {
                changeLogRule.configure(ruleConfig);
                if (evaluateCondition(ruleConfig, change) && changeLogRule.invalid(databaseChangeLog)) {
                    handleViolation(changeLogRule.getMessage(databaseChangeLog), changeLogRule.getName(), ruleConfig);
                }
            }
        }

        private void handleViolation(String errorMessage, String rule, RuleConfig ruleConfig) throws ChangeLogParseException {
            if (!isEnabledFrom(ruleConfig) || isIgnored(rule)) {
                reportItems.add(ReportItem.ignored(databaseChangeLog, changeSet, rule, errorMessage));
                return;
            }
            if (config.isFailFast()) {
                throw ChangeLogParseExceptionHelper.build(databaseChangeLog, changeSet, errorMessage);
            } else {
                reportItems.add(ReportItem.error(databaseChangeLog, changeSet, rule, errorMessage));
            }
        }

        private boolean evaluateCondition(RuleConfig ruleConfig, Change change) {
            return ruleConfig.getConditionalExpression()
                .map(expression -> expression.getValue(change, boolean.class))
                .orElse(true);
        }

        private boolean isIgnored(String ruleName) {
            if (changeSet == null || changeSet.getComments() == null || !changeSet.getComments().contains(LQL_IGNORE_TOKEN)) {
                return false;
            }
            final String comments = changeSet.getComments();
            final String toIgnore = comments.substring(comments.indexOf(LQL_IGNORE_TOKEN) + LQL_IGNORE_TOKEN.length());
            final String[] split = toIgnore.split(",");
            return Arrays.stream(split).anyMatch(ruleName::equalsIgnoreCase);
        }

        private boolean isEnabledFrom(RuleConfig ruleConfig) {
            if (!config.isEnabledFrom() && !ruleConfig.isEnabledFrom()) {
                return true;
            }
            return !config.isEnabledFrom() || filesParsed.contains(config.getEnableFrom());
        }
    }

}
