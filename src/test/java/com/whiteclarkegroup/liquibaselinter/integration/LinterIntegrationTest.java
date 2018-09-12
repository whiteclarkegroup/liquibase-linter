package com.whiteclarkegroup.liquibaselinter.integration;

import com.google.common.io.CharStreams;
import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.Writer;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class LinterIntegrationTest {

    abstract List<IntegrationTestConfig> getTests();

    @TestFactory
    Stream<DynamicTest> dynamicTests() {
        ThrowingConsumer<IntegrationTestConfig> testExecutor = running -> {
            final Liquibase liquibase = LiquibaseIntegrationTestResolver.buildLiquibase(running.getChangeLogFile(), running.getConfigFile());
            final Writer nullWriter = CharStreams.nullWriter();
            final Contexts contexts = new Contexts();
            if (running.getMessage() != null) {
                ChangeLogParseException changeLogParseException = assertThrows(ChangeLogParseException.class, () -> liquibase.update(contexts, nullWriter));
                assertTrue(changeLogParseException.getMessage().contains(running.getMessage()));

            } else {
                liquibase.update(contexts, nullWriter);
            }
        };
        return DynamicTest.stream(getTests().iterator(), IntegrationTestConfig::getDisplayName, testExecutor);
    }

    @AfterEach
    void tearDown() {
        ChangeLogParserFactory.reset();
    }

}
