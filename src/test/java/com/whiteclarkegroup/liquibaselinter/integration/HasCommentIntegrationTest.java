package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class HasCommentIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with no comment xml",
            "has-comment/has-comment-fail.xml",
            "has-comment/lqllint.json",
            "Change set must have a comment");

        shouldPass(
            "Should pass with a comment xml",
            "has-comment/has-comment-pass.xml",
            "has-comment/lqllint.json");

        shouldFail(
            "Should fail with no comment json",
            "has-comment/has-comment-fail.json",
            "has-comment/lqllint.json",
            "Change set must have a comment");

        shouldPass(
            "Should pass with a comment json",
            "has-comment/has-comment-pass.xml",
            "has-comment/lqllint.json");

        shouldFail(
            "Should fail with no comment yaml",
            "has-comment/has-comment-fail.yaml",
            "has-comment/lqllint.json",
            "Change set must have a comment");

        shouldPass(
            "Should pass with a comment yaml",
            "has-comment/has-comment-pass.yaml",
            "has-comment/lqllint.json");
    }

}
