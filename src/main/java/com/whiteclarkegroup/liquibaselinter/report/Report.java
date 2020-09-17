package com.whiteclarkegroup.liquibaselinter.report;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;

import java.util.ArrayList;
import java.util.List;

public class Report {

    private final List<ReportItem> items = new ArrayList<>();
    private int disabledRuleCount;

    public List<ReportItem> getItems() {
        return items;
    }

    public long countErrors() {
        return count(ReportItem.ReportItemType.ERROR);
    }

    public long countIgnored() {
        return count(ReportItem.ReportItemType.IGNORED);
    }

    public long countSkipped() {
        return count(ReportItem.ReportItemType.SKIPPED);
    }

    public long countPassed() {
        return count(ReportItem.ReportItemType.PASSED);
    }

    private long count(ReportItem.ReportItemType type) {
        return items.stream().filter(item -> item.getType() == type).count();
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public void merge(Report report) {
        items.addAll(report.getItems());
    }

    public void addIgnored(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message) {
        items.add(ReportItem.ignored(databaseChangeLog, changeSet, rule, message));
    }

    public void addError(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message) {
        items.add(ReportItem.error(databaseChangeLog, changeSet, rule, message));
    }

    public void addSkipped(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message) {
        items.add(ReportItem.skipped(databaseChangeLog, changeSet, rule, message));
    }

    public void addPassed(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message) {
        items.add(ReportItem.passed(databaseChangeLog, changeSet, rule, message));
    }

    public int getDisabledRuleCount() {
        return disabledRuleCount;
    }

    public void setDisabledRuleCount(int disabledRuleCount) {
        this.disabledRuleCount = disabledRuleCount;
    }
}
