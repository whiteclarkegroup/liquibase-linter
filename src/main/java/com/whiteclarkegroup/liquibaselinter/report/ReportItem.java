package com.whiteclarkegroup.liquibaselinter.report;

import liquibase.change.Change;
import liquibase.changelog.DatabaseChangeLog;

public class ReportItem {

    public enum ReportItemType {
        ERROR,
        IGNORED
    }

    private final String filePath;
    private final String changeSetId;
    private final String rule;
    private final ReportItemType type;
    private final String message;

    public static ReportItem error(DatabaseChangeLog databaseChangeLog, Change change, String rule, String message) {
        return new ReportItem(getFilePath(databaseChangeLog, change), getChangeSetId(change), rule, ReportItemType.ERROR, message);
    }

    public static ReportItem ignored(DatabaseChangeLog databaseChangeLog, Change change, String rule, String message) {
        return new ReportItem(getFilePath(databaseChangeLog, change), getChangeSetId(change), rule, ReportItemType.IGNORED, message);
    }

    private static String getChangeSetId(Change change) {
        if (change != null) {
            return change.getChangeSet().getId();
        }
        return null;
    }

    private static String getFilePath(DatabaseChangeLog databaseChangeLog, Change change) {
        if (change != null) {
            return change.getChangeSet().getFilePath();
        } else if (databaseChangeLog != null) {
            return databaseChangeLog.getFilePath();
        } else {
            return "Generic rule";
        }
    }

    private ReportItem(String filePath, String changeSetId, String rule, ReportItemType type, String message) {
        this.filePath = filePath;
        this.changeSetId = changeSetId;
        this.rule = rule;
        this.type = type;
        this.message = message;
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
}
