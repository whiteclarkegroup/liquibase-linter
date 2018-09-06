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
                "duplicate-includes.xml",
                "duplicate-includes.json",
                "liquibase.exception.SetupException: Changelog file 'src/test/resources/integration/duplicate-includes-change.xml' was included more than once");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldFail(
                "Should not allow duplicate file includes nested",
                "duplicate-includes-nested.xml",
                "duplicate-includes.json",
                "liquibase.exception.SetupException: Changelog file 'src/test/resources/integration/duplicate-includes-change.xml' was included more than once");

        return Arrays.asList(test1, test2);
    }

}
