package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class HasContextIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should not pass with no context value",
            "has-context-fail.xml",
            "has-context.json",
            "Should have at least one context on the change set");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass with a context value",
            "has-context-pass.xml",
            "has-context.json");

        return Arrays.asList(test1, test2);
    }

}
