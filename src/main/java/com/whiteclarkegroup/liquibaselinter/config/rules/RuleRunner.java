package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.google.common.base.Strings;
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
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class RuleRunner {

    private static final String LQL_IGNORE_TOKEN = "lql-ignore";
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
                    if (isEnabled(ruleConfig)) {
                        changeRule.configure(ruleConfig);
                        if (ConditionHelper.evaluateCondition(ruleConfig, change) && changeRule.invalid(change)) {
                            handleViolation(changeRule.getMessage(change), changeRule.getName(), change.getChangeSet().getChangeLog(), change.getChangeSet());
                        }
                    }
                }
            }
        }
    }

    public void checkChangeSet(ChangeSet changeSet) throws ChangeLogParseException {
        for (ChangeSetRule changeSetRule : changeSetRules) {
            final List<RuleConfig> configs = config.forRule(changeSetRule.getName());
            for (RuleConfig ruleConfig : configs) {
                if (isEnabled(ruleConfig)) {
                    changeSetRule.configure(ruleConfig);
                    if (ConditionHelper.evaluateCondition(ruleConfig, changeSet) && changeSetRule.invalid(changeSet)) {
                        handleViolation(changeSetRule.getMessage(changeSet), changeSetRule.getName(), changeSet.getChangeLog(), changeSet);
                    }
                }
            }
        }
    }

    public void checkChangeLog(DatabaseChangeLog databaseChangeLog) throws ChangeLogParseException {
        for (ChangeLogRule changeLogRule : changeLogRules) {
            final List<RuleConfig> configs = config.forRule(changeLogRule.getName());
            for (RuleConfig ruleConfig : configs) {
                if (isEnabled(ruleConfig)) {
                    changeLogRule.configure(ruleConfig);
                    if (ConditionHelper.evaluateCondition(ruleConfig, databaseChangeLog) && changeLogRule.invalid(databaseChangeLog)) {
                        handleViolation(changeLogRule.getMessage(databaseChangeLog), changeLogRule.getName(), databaseChangeLog, null);
                    }
                }
            }
        }
    }

    private void handleViolation(String errorMessage, String rule, DatabaseChangeLog databaseChangeLog, ChangeSet changeSet) throws ChangeLogParseException {
        if (isIgnored(rule, changeSet)) {
            report.addIgnored(databaseChangeLog, changeSet, rule, errorMessage);
        } else if (config.isFailFast()) {
            throw ChangeLogParseExceptionHelper.build(databaseChangeLog, changeSet, errorMessage);
        } else {
            report.addError(databaseChangeLog, changeSet, rule, errorMessage);
        }
    }

    private boolean isIgnored(String ruleName, ChangeSet changeSet) {
        final String comments = Optional.ofNullable(changeSet).map(ChangeSet::getComments).orElse("");
        if (comments.endsWith(LQL_IGNORE_TOKEN)) {
            return true;
        }
        int index = comments.indexOf(LQL_IGNORE_TOKEN + ':'); // see if this specific rule is ignored
        if (index >= 0) {
            final String toIgnore = comments.substring(index + LQL_IGNORE_TOKEN.length() + 1);
            return Arrays.stream(toIgnore.split(",")).anyMatch(ruleName::equalsIgnoreCase);
        }
        return false;
    }

    private boolean isEnabled(RuleConfig ruleConfig) {
        return ruleConfig.isEnabled()
            && (Strings.isNullOrEmpty(ruleConfig.getEnableAfter()) || filesParsed.contains(ruleConfig.getEnableAfter()));
    }

}
