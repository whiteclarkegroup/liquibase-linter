package com.whiteclarkegroup.liquibaselinter;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

public class ChangeLogParseExceptionHelper {

    private ChangeLogParseExceptionHelper() {

    }

    public static ChangeLogParseException build(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String customMessage) {
        if (changeSet != null) {
            final String message = "File name: " + changeSet.getFilePath() + " -- Change Set ID: " + changeSet.getId() + " -- Author: " + changeSet.getAuthor() + " -- Message: " + customMessage;
            return new ChangeLogParseException(message);
        } else if (databaseChangeLog != null) {
            return build(databaseChangeLog, customMessage);
        }
        return new ChangeLogParseException(customMessage);
    }

    private static ChangeLogParseException build(DatabaseChangeLog databaseChangeLog, String customMessage) {
        final String message = "File name: " + databaseChangeLog.getFilePath() + " -- Message: " + customMessage;
        return new ChangeLogParseException(message);
    }

}
