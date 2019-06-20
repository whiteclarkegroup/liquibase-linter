package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class AggregateErrorsIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should aggregate errors",
            "aggregate-errors/aggregate-errors.xml",
            "aggregate-errors/aggregate-errors.json",
            "Linting failed with 3 errors"
        );
    }

}
