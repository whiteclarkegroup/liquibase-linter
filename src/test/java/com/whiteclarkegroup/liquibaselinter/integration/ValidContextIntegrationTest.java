package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ValidContextIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail with invalid context value",
            "valid-context/valid-context-fail.xml",
            "valid-context/lqllint.json",
            "Context is incorrect, should end with '_test' or '_script'");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass with valid context value",
            "valid-context/valid-context-pass.xml",
            "valid-context/lqllint.json");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldPass(
            "Should pass with no context value",
            "valid-context/valid-no-context-value-pass.xml",
            "valid-context/lqllint.json");

        return Arrays.asList(test1, test2, test3);
    }

}
