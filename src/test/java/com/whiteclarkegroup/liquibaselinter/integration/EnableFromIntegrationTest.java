package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class EnableFromIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with no comment as enabled after fail 1",
            "enable-from/root.xml",
            "enable-from/lqllint-from-fail-1.json",
            "src/test/resources/integration/enable-from/has-comment-fail-2.xml");

        shouldPass(
            "Should pass with a comment as rule not enabled with first two",
            "enable-from/root.xml",
            "enable-from/lqllint-from-fail-2.json");
    }

}
