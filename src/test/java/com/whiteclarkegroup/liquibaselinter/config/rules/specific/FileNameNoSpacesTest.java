package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileNameNoSpacesTest {

    private FileNameNoSpaces fileNameNoSpaces;

    @BeforeEach
    void setUp() {
        fileNameNoSpaces = new FileNameNoSpaces(null);
    }

    @DisplayName("Filename with spaces should be invalid")
    @Test
    void filenameWithSpacesShouldBeInvalid() {
        assertTrue(fileNameNoSpaces.invalid(withFilePath("//test/dir/this has spaces.xml"), null));
    }

    @DisplayName("Filename with spaces should be invalid")
    @Test
    void filenameWithoutSpacesShouldBeValid() {
        assertFalse(fileNameNoSpaces.invalid(withFilePath("//test/dir/this-has-spaces.xml"), null));
    }

    private DatabaseChangeLog withFilePath(String filePath) {
        final DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog();
        databaseChangeLog.setLogicalFilePath(filePath);
        return databaseChangeLog;
    }
}
