package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeLogRule;
import liquibase.changelog.DatabaseChangeLog;

import java.util.Optional;

public class FileNameNoSpacesRuleImpl extends AbstractLintRule implements ChangeLogRule {
    private static final String NAME = "file-name-no-spaces";
    private static final String MESSAGE = "Changelog filenames should not contain spaces";

    public FileNameNoSpacesRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(DatabaseChangeLog changeLog) {
        final Optional<String> path = Optional.ofNullable(changeLog.getFilePath());
        return path.isPresent() && path.get().contains(" ");
    }
}
