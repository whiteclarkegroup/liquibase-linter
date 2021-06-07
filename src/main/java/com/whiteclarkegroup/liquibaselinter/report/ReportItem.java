package com.whiteclarkegroup.liquibaselinter.report;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;

public final class ReportItem {

    private final String filePath;
    private final String changeSetId;
    private final String rule;
    private final ReportItemType type;
    private final String message;

    ReportItem(String filePath, String changeSetId, String rule, ReportItemType type, String message) {
        this.filePath = filePath;
        this.changeSetId = changeSetId;
        this.rule = rule;
        this.type = type;
        this.message = message;
    }

    public static ReportItem error(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message) {
        return create(databaseChangeLog, changeSet, rule, message, ReportItemType.ERROR);
    }

    public static ReportItem ignored(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message) {
        return create(databaseChangeLog, changeSet, rule, message, ReportItemType.IGNORED);
    }

    public static ReportItem passed(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message) {
        return create(databaseChangeLog, changeSet, rule, message, ReportItemType.PASSED);
    }

    private static ReportItem create(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message, ReportItemType type) {
        return new ReportItem(getFilePath(databaseChangeLog, changeSet), changeSet == null ? null : changeSet.getId(), rule, type, message);
    }

    private static String getFilePath(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet) {
        if (changeSet != null) {
            return changeSet.getFilePath();
        } else if (databaseChangeLog != null) {
            return databaseChangeLog.getFilePath();
        }
        return null;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getChangeSetId() {
        return changeSetId;
    }

    public String getRule() {
        return rule;
    }

    public ReportItemType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public enum ReportItemType {
        ERROR,
        IGNORED,
        PASSED
    }
}
