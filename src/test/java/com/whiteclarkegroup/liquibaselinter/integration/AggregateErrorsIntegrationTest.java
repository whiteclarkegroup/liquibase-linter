package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class AggregateErrorsIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should aggregate errors",
            "aggregate-errors/aggregate-errors.xml",
            "aggregate-errors/aggregate-errors.json",
            "Linting failed with 3 errors");

        return Collections.singletonList(test1);
    }

}
