package com.whiteclarkegroup.liquibaselinter;

import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

public class ChangeLogParseExceptionHelper {

    private ChangeLogParseExceptionHelper() {

    }

    public static ChangeLogParseException build(DatabaseChangeLog databaseChangeLog, Change change, String customMessage) {
        if (change != null) {
            return build(change.getChangeSet(), customMessage);
        } else if (databaseChangeLog != null) {
            return build(databaseChangeLog, customMessage);
        }
        return new ChangeLogParseException(customMessage);
    }

    private static ChangeLogParseException build(DatabaseChangeLog databaseChangeLog, String customMessage) {
        final String message = "File name: " + databaseChangeLog.getFilePath() + " -- Message: " + customMessage;
        return new ChangeLogParseException(message);
    }

    private static ChangeLogParseException build(ChangeSet changeSet, String customMessage) {
        final String message = "File name: " + changeSet.getFilePath() + " -- Change Set ID: " + changeSet.getId() + " -- Author: " + changeSet.getAuthor() + " -- Message: " + customMessage;
        return new ChangeLogParseException(message);
    }

}
