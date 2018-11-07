package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class DropNotNullRequireColumnDataTypeIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail with invalid context value",
            "drop-not-null-require-column-data-type/drop-not-null-require-column-data-type-fail.xml",
            "drop-not-null-require-column-data-type/lqllint.json",
            "Drop not null constraint column data type attribute must be populated");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass with valid context value",
            "drop-not-null-require-column-data-type/drop-not-null-require-column-data-type-pass.xml",
            "drop-not-null-require-column-data-type/lqllint.json");

        return Arrays.asList(test1, test2);
    }

}
