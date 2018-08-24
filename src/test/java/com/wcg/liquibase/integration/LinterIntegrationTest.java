package com.wcg.liquibase.integration;

import com.google.common.io.CharStreams;
import com.wcg.liquibase.resolvers.LiquibaseIntegrationTestResolver;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class LinterIntegrationTest {

    abstract List<IntegrationTestConfig> getTests();

    @TestFactory
    Stream<DynamicTest> dynamicTests() {
        ThrowingConsumer<IntegrationTestConfig> testExecutor = running -> {
            Liquibase liquibase = LiquibaseIntegrationTestResolver.buildLiquibase(running.getChangeLogFile(), running.getConfigFile());
            if (running.getMessage() != null) {
                ChangeLogParseException changeLogParseException = assertThrows(ChangeLogParseException.class, () -> liquibase.update(new Contexts(), CharStreams.nullWriter()));
                assertTrue(changeLogParseException.getMessage().contains("src/test/resources/integration/file-name no-spaces.xml -- Message: Changelog filenames should not contain spaces"));
            } else {
                liquibase.update(new Contexts(), CharStreams.nullWriter());
            }
        };
        return DynamicTest.stream(getTests().iterator(), IntegrationTestConfig::getDisplayName, testExecutor);
    }

}
