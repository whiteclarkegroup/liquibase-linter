package com.wcg.liquibase.integration;

import com.google.common.io.CharStreams;
import com.wcg.liquibase.resolvers.LiquibaseLinterIntegrationTest;
import com.wcg.liquibase.resolvers.LiquibaseIntegrationTestResolver;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class FileNameNoSpacesIntegrationTest {

    @DisplayName("Should not allow file name with spaces")
    @Test
    @LiquibaseLinterIntegrationTest(changeLogFile = "file-name no-spaces.xml", configFile = "file-name-no-spaces.json")
    void should_not_allow_file_name_with_spaces(Liquibase liquibase) {
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> liquibase.update(new Contexts(), CharStreams.nullWriter()));
        assertTrue(changeLogParseException.getMessage().contains("src/test/resources/integration/file-name no-spaces.xml -- Message: Changelog filenames should not contain spaces"));
    }

}
