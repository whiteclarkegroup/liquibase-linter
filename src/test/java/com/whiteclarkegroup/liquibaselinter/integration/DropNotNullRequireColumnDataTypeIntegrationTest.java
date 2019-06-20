package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class DropNotNullRequireColumnDataTypeIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with invalid context value",
            "drop-not-null-require-column-data-type/drop-not-null-require-column-data-type-fail.xml",
            "drop-not-null-require-column-data-type/lqllint.json",
            "Drop not null constraint column data type attribute must be populated");

        shouldPass(
            "Should pass with valid context value",
            "drop-not-null-require-column-data-type/drop-not-null-require-column-data-type-pass.xml",
            "drop-not-null-require-column-data-type/lqllint.json");
    }

}
