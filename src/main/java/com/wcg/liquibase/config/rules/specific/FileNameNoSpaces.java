package com.wcg.liquibase.config.rules.specific;

import com.wcg.liquibase.config.rules.Rule;
import com.wcg.liquibase.config.rules.RuleConfig;
import liquibase.change.Change;
import liquibase.changelog.DatabaseChangeLog;

import java.util.Optional;

public class FileNameNoSpaces extends Rule<DatabaseChangeLog> {

    public FileNameNoSpaces(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(DatabaseChangeLog databaseChangeLog, Change change) {
        final Optional<String> path = Optional.ofNullable(databaseChangeLog.getFilePath());
        return path.isPresent() && path.get().contains(" ");
    }

}
