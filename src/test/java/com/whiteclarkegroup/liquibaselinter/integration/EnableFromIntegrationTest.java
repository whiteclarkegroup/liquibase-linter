package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class EnableFromIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with no comment as enabled after fail 1",
            "enable-from/root-enable-from/root.xml",
            "enable-from/root-enable-from/lqllint-from-fail-1.json",
            "src/test/resources/integration/enable-from/root-enable-from/has-comment-fail-2.xml");

        shouldPass(
            "Should pass with a comment as rule not enabled with first two",
            "enable-from/root-enable-from/root.xml",
            "enable-from/root-enable-from/lqllint-from-fail-2.json");

        shouldFail(
            "Should fail with no comment as enabled after fail 1 for rule",
            "enable-from/rule-enable-from/root.xml",
            "enable-from/rule-enable-from/lqllint-from-fail-1.json",
            "src/test/resources/integration/enable-from/rule-enable-from/has-comment-fail-2.xml");

        shouldPass(
            "Should pass with a comment as rule not enabled with first two for rule",
            "enable-from/rule-enable-from/root.xml",
            "enable-from/rule-enable-from/lqllint-from-fail-2.json");

        shouldPass(
            "Should pass with a comment as rule not enabled as overridden with rule enable from",
            "enable-from/override-enable-from/root.xml",
            "enable-from/override-enable-from/lqllint.json");

        shouldFail(
            "Should fail with no comment as enabled after nested",
            "enable-from/nested/root.xml",
            "enable-from/nested/lqllint-fail.json",
            "src/test/resources/integration/enable-from/nested/core/core-has-comment-fail.xml");

        shouldPass(
            "Should pass with a comment as rule not enabled nested",
            "enable-from/nested/root.xml",
            "enable-from/nested/lqllint-pass.json");
    }

}
