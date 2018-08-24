package com.wcg.liquibase.integration;

class IntegrationTestConfig {
    private final String displayName;
    private final String changeLogFile;
    private final String configFile;
    private final String message;

    IntegrationTestConfig(String displayName, String changeLogFile, String configFile) {
        this.displayName = displayName;
        this.changeLogFile = changeLogFile;
        this.configFile = configFile;
        this.message = null;
    }

    IntegrationTestConfig(String displayName, String changeLogFile, String configFile, String message) {
        this.displayName = displayName;
        this.changeLogFile = changeLogFile;
        this.configFile = configFile;
        this.message = message;
    }

    String getDisplayName() {
        return displayName;
    }

    String getChangeLogFile() {
        return changeLogFile;
    }

    String getConfigFile() {
        return configFile;
    }

    String getMessage() {
        return message;
    }
}
