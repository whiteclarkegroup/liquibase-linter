package com.wcg.liquibase.integration;

class IntegrationTestConfig {
    private final String displayName;
    private final String changeLogFile;
    private final String configFile;
    private final String message;

    private IntegrationTestConfig(String displayName, String changeLogFile, String configFile, String message) {
        this.displayName = displayName;
        this.changeLogFile = changeLogFile;
        this.configFile = configFile;
        this.message = message;
    }

    public static IntegrationTestConfig shouldPass(String displayName, String changeLogFile, String configFile) {
        return new IntegrationTestConfig(displayName, changeLogFile, configFile, null);
    }

    public static IntegrationTestConfig shouldFail(String displayName, String changeLogFile, String configFile, String message) {
        return new IntegrationTestConfig(displayName, changeLogFile, configFile, message);
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
