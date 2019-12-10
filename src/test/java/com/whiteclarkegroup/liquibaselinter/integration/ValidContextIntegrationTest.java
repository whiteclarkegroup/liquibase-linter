package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ValidContextIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with invalid context value",
            "valid-context/valid-context-fail.xml",
            "valid-context/lqlint.json",
            "Context is incorrect, should end with '_test' or '_script'");

        shouldPass(
            "Should pass with valid context value",
            "valid-context/valid-context-pass.xml",
            "valid-context/lqlint.json");

        shouldPass(
            "Should pass with no context value",
            "valid-context/valid-no-context-value-pass.xml",
            "valid-context/lqlint.json");
    }

}
