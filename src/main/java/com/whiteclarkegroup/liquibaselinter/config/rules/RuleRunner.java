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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
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

    public RuleRunner(Config config) {
        this.config = config;
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

    public RunningContext forChange(Change change) {
        return new RunningContext(config, changeRules, null, changeLogRules, change, change.getChangeSet().getChangeLog(), report.getReportItems(), change.getChangeSet());
    }

    public RunningContext forDatabaseChangeLog(DatabaseChangeLog databaseChangeLog) {
        return new RunningContext(config, null, null, changeLogRules, null, databaseChangeLog, report.getReportItems(), null);
    }

    public RunningContext forChangeSet(ChangeSet changeSet) {
        return new RunningContext(config, null, changeSetRules, null, null, changeSet.getChangeLog(), report.getReportItems(), changeSet);
    }

    public RunningContext forGeneric() {
        return new RunningContext(config, null, null, null, null, null, report.getReportItems(), null);
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

        private RunningContext(Config config, List<ChangeRule> changeRules, List<ChangeSetRule> changeSetRules, List<ChangeLogRule> changeLogRules, Change change, DatabaseChangeLog databaseChangeLog, Collection<ReportItem> reportItems, ChangeSet changeSet) {
            this.config = config;
            this.changeRules = changeRules;
            this.changeSetRules = changeSetRules;
            this.changeLogRules = changeLogRules;
            this.change = change;
            this.databaseChangeLog = databaseChangeLog;
            this.reportItems = reportItems;
            this.changeSet = changeSet;
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
                for (RuleConfig config : configs) {
                    changeRule.configure(config);
                    if (evaluateCondition(config, change) && changeRule.invalid(change)) {
                        handleViolation(changeRule.getMessage(change), changeRule.getName());
                    } else {
                        reportItems.add(ReportItem.passed(databaseChangeLog, changeSet, changeRule.getName(), changeRule.getMessage(change)));
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
            for (RuleConfig config : configs) {
                changeSetRule.configure(config);
                if (evaluateCondition(config, change) && changeSetRule.invalid(changeSet)) {
                    handleViolation(changeSetRule.getMessage(changeSet), changeSetRule.getName());
                } else {
                    reportItems.add(ReportItem.passed(databaseChangeLog, changeSet, changeSetRule.getName(), changeSetRule.getMessage(changeSet)));
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
            for (RuleConfig config : configs) {
                changeLogRule.configure(config);
                if (evaluateCondition(config, change) && changeLogRule.invalid(databaseChangeLog)) {
                    handleViolation(changeLogRule.getMessage(databaseChangeLog), changeLogRule.getName());
                } else {
                    reportItems.add(ReportItem.passed(databaseChangeLog, changeSet, changeLogRule.getName(), changeLogRule.getMessage(databaseChangeLog)));
                }
            }
        }

        private void handleViolation(String errorMessage, String rule) throws ChangeLogParseException {
            if (isIgnored(rule)) {
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
    }

}
