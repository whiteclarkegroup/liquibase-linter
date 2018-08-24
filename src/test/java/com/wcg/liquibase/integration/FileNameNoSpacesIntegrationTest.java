package com.wcg.liquibase.integration;

import com.google.common.io.CharStreams;
import com.wcg.liquibase.resolvers.LiquibaseIntegrationTestResolver;
import com.wcg.liquibase.resolvers.LiquibaseLinterIntegrationTest;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
protected class FileNameNoSpacesIntegrationTest extends LinterIntegrationTest {

    @DisplayName("Should not allow file name with spaces")
    @LiquibaseLinterIntegrationTest(changeLogFile = "file-name no-spaces.xml", configFile = "file-name-no-spaces.json")
    void shouldNotAllowFileNameWithSpaces(Liquibase liquibase) {
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> liquibase.update(new Contexts(), CharStreams.nullWriter()));
        assertTrue(changeLogParseException.getMessage().contains("src/test/resources/integration/file-name no-spaces.xml -- Message: Changelog filenames should not contain spaces"));
    }

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = new IntegrationTestConfig(
                "Should not allow file name with spaces",
                "file-name no-spaces.xml",
                "file-name-no-spaces.json",
                "src/test/resources/integration/file-name no-spaces.xml -- Message: Changelog filenames should not contain spaces");

        IntegrationTestConfig test2 = new IntegrationTestConfig(
                "Should not allow included file with name that has spaces",
                "file-name-no-spaces.xml",
                "file-name-no-spaces.json",
                "src/test/resources/integration/file-name no-spaces.xml -- Message: Changelog filenames should not contain spaces");

        IntegrationTestConfig test3 = new IntegrationTestConfig(
                "Should allow file name without spaces",
                "file-name-no-spaces-valid.xml",
                "file-name-no-spaces.json");

        return Arrays.asList(test1, test2, test3);
    }

}
