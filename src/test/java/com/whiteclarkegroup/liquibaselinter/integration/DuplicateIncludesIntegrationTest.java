package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class DuplicateIncludesIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
                "Should not allow duplicate file includes",
            "duplicate-includes/duplicate-includes.xml",
            "duplicate-includes/duplicate-includes.json",
            "liquibase.exception.SetupException: Changelog file 'src/test/resources/integration/duplicate-includes/duplicate-includes-change.xml' was included more than once");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldFail(
                "Should not allow duplicate file includes nested",
            "duplicate-includes/duplicate-includes-nested.xml",
            "duplicate-includes/duplicate-includes.json",
            "liquibase.exception.SetupException: Changelog file 'src/test/resources/integration/duplicate-includes/duplicate-includes-change.xml' was included more than once");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldPass(
            "Should not mark as duplicate include when the file contains no change sets",
            "duplicate-includes/duplicate-includes-no-change-set.xml",
            "duplicate-includes/duplicate-includes.json");

        return Arrays.asList(test1, test2, test3);
    }

}
