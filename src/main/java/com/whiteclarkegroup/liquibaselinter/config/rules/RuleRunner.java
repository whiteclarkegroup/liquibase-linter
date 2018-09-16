package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.whiteclarkegroup.liquibaselinter.ChangeLogParseExceptionHelper;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.ReportItem;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.servicelocator.DefaultPackageScanClassResolver;
import liquibase.servicelocator.PackageScanClassResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RuleRunner {
    private static final String CORE_RULES_PACKAGE = "com/whiteclarkegroup/liquibaselinter/config/rules/core";
    private final PackageScanClassResolver packageScanner = new DefaultPackageScanClassResolver();

    private final Config config;
    private final List<LintRule> allRules;
    private final List<ChangeRule> changeRules;
    private final List<ChangeSetRule> changeSetRules;
    private final Report report = new Report();

    public RuleRunner(Config config) {
        this.config = config;
        this.allRules = discoverRules();
        this.changeRules = assembleChangeRules();
        this.changeSetRules = assembleChangeSetRules();
    }

    private List<LintRule> discoverRules() {
        return packageScanner.findImplementations(LintRule.class, CORE_RULES_PACKAGE).stream()
            .map(found -> {
                try {
                    Class<? extends LintRule> clazz = (Class<? extends LintRule>) found;
                    return clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    return null;
                }
            })
            .filter(lintRule -> {
                if (lintRule != null && config.isRuleEnabled(lintRule.getName())) {
                    lintRule.configure(config.getRules().get(lintRule.getName()));
                    return true;
                };
                return false;
            })
            .collect(Collectors.toList());
    }

    private List<ChangeRule> assembleChangeRules() {
        return allRules.stream()
            .filter(lintRule -> ChangeRule.class.isAssignableFrom(lintRule.getClass()))
            .map(lintRule -> (ChangeRule) lintRule)
            .collect(Collectors.toList());
    }

    private List<ChangeSetRule> assembleChangeSetRules() {
        return allRules.stream()
            .filter(lintRule -> ChangeSetRule.class.isAssignableFrom(lintRule.getClass()))
            .map(lintRule -> (ChangeSetRule) lintRule)
            .collect(Collectors.toList());
    }

    public Report getReport() {
        return report;
    }

    public RunningContext forChange(Change change) {
        return new RunningContext(config, changeRules, null, change, change.getChangeSet().getChangeLog(), report.getReportItems(), change.getChangeSet());
    }

    public RunningContext forDatabaseChangeLog(DatabaseChangeLog databaseChangeLog) {
        return new RunningContext(config, null, null, null, databaseChangeLog, report.getReportItems(), null);
    }

    public RunningContext forChangeSet(ChangeSet changeSet) {
        return new RunningContext(config, null, changeSetRules,null, changeSet.getChangeLog(), report.getReportItems(), changeSet);
    }

    public RunningContext forGeneric() {
        return new RunningContext(config, null, null,null, null, report.getReportItems(), null);
    }

    @SuppressWarnings("unchecked")
    public static class RunningContext {

        private static final String LQL_IGNORE_TOKEN = "lql-ignore:";
        private final Config config;
        private final List<ChangeRule> changeRules;
        private final List<ChangeSetRule> changeSetRules;
        private final Change change;
        private final DatabaseChangeLog databaseChangeLog;
        private final Collection<ReportItem> reportItems;
        private final ChangeSet changeSet;

        private RunningContext(Config config, List<ChangeRule> changeRules, List<ChangeSetRule> changeSetRules, Change change, DatabaseChangeLog databaseChangeLog, Collection<ReportItem> reportItems, ChangeSet changeSet) {
            this.config = config;
            this.changeRules = changeRules;
            this.changeSetRules = changeSetRules;
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
            if (changeRule.getChangeType().isAssignableFrom(change.getClass())
                && changeRule.supports(change)
                && changeRule.invalid(change)
                && shouldApply(changeRule.getConfig(), changeRule.getName(), changeRule.getMessage(change))) {
                handleError(changeRule.getMessage(change), changeRule.getName());
            }
        }

        public RunningContext checkChangeSet() throws ChangeLogParseException {
            for (ChangeSetRule changeSetRule : changeSetRules) {
                checkChangeSetRule(changeSetRule);
            }
            return this;
        }

        private void checkChangeSetRule(ChangeSetRule changeSetRule) throws ChangeLogParseException {
            if (changeSetRule.invalid(changeSet) && shouldApply(changeSetRule.getConfig(), changeSetRule.getName(), changeSetRule.getMessage(changeSet))) {
                handleError(changeSetRule.getMessage(changeSet), changeSetRule.getName());
            }
        }

        @SuppressWarnings("unchecked")
        public RunningContext run(RuleType ruleType, Object object) throws ChangeLogParseException {
            final Optional<Rule> optionalRule = ruleType.create(config.getRules());
            if (optionalRule.isPresent()) {
                Rule rule = optionalRule.get();
                final String errorMessage = getErrorMessage(ruleType, object, rule);
                if (rule.getRuleConfig().isEnabled() && shouldApply(rule.getRuleConfig(), ruleType.getKey(), errorMessage) && rule.invalid(object, change)) {
                    handleError(errorMessage, ruleType.getKey());
                }
            }
            return this;
        }

        private void handleError(String errorMessage, String rule) throws ChangeLogParseException {
            if (config.isFailFast()) {
                throw ChangeLogParseExceptionHelper.build(databaseChangeLog, changeSet, errorMessage);
            } else {
                reportItems.add(ReportItem.error(databaseChangeLog, changeSet, rule, errorMessage));
            }
        }

        private String getErrorMessage(RuleType ruleType, Object object, Rule rule) {
            final String errorMessage = Optional.ofNullable(rule.getErrorMessage()).orElse(ruleType.getDefaultErrorMessage());
            if (rule instanceof WithFormattedErrorMessage) {
                return ((WithFormattedErrorMessage) rule).formatErrorMessage(errorMessage, object);
            }
            return errorMessage;
        }

        private boolean shouldApply(RuleConfig ruleConfig, String ruleKey, String errorMessage) {
            final boolean ignored = isIgnored(ruleKey);
            if (ignored) {
                reportItems.add(ReportItem.ignored(databaseChangeLog, changeSet, ruleKey, errorMessage));
            }
            return evaluateCondition(ruleConfig, change) && !ignored;
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
