package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class EnableAfterIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with no comment as enabled after fail 1",
            "enable-after/root-enable-after/root.xml",
            "enable-after/root-enable-after/lqlint-after-fail-1.json",
            "integration/enable-after/root-enable-after/has-comment-fail-2.xml");

        shouldPass(
            "Should pass with a comment as rule not enabled with first two",
            "enable-after/root-enable-after/root.xml",
            "enable-after/root-enable-after/lqlint-after-fail-2.json");

        shouldFail(
            "Should fail with no comment as enabled after fail 1 for rule",
            "enable-after/rule-enable-after/root.xml",
            "enable-after/rule-enable-after/lqlint-after-fail-1.json",
            "integration/enable-after/rule-enable-after/has-comment-fail-2.xml");

        shouldPass(
            "Should pass with a comment as rule not enabled with first two for rule",
            "enable-after/rule-enable-after/root.xml",
            "enable-after/rule-enable-after/lqlint-after-fail-2.json");

        shouldPass(
            "Should pass with a comment as rule not enabled as overridden with rule enable after",
            "enable-after/override-enable-after/root.xml",
            "enable-after/override-enable-after/lqlint.json");

        shouldFail(
            "Should fail with no comment as enabled after nested",
            "enable-after/nested/root.xml",
            "enable-after/nested/lqlint-fail.json",
            "integration/enable-after/nested/core/core-has-comment-fail.xml");

        shouldPass(
            "Should pass with a comment as rule not enabled nested",
            "enable-after/nested/root.xml",
            "enable-after/nested/lqlint-pass.json");

        shouldFail(
            "Should fail with multiple configs",
            "enable-after/rule-enable-after-multiple-configs/root.xml",
            "enable-after/rule-enable-after-multiple-configs/lqlint-fail.json",
            "integration/enable-after/rule-enable-after-multiple-configs/object-name-fail-2.xml");

        shouldPass(
            "Should pass with multiple rule configs",
            "enable-after/rule-enable-after-multiple-configs/root.xml",
            "enable-after/rule-enable-after-multiple-configs/lqlint-pass.json");
    }

}
