package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class HasCommentIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail with no comment xml",
            "has-comment/has-comment-fail.xml",
            "has-comment/lqllint.json",
            "Change set must have a comment");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass with a comment xml",
            "has-comment/has-comment-pass.xml",
            "has-comment/lqllint.json");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldFail(
            "Should fail with no comment json",
            "has-comment/has-comment-fail.json",
            "has-comment/lqllint.json",
            "Change set must have a comment");

        IntegrationTestConfig test4 = IntegrationTestConfig.shouldPass(
            "Should pass with a comment json",
            "has-comment/has-comment-pass.xml",
            "has-comment/lqllint.json");

        IntegrationTestConfig test5 = IntegrationTestConfig.shouldFail(
            "Should fail with no comment yaml",
            "has-comment/has-comment-fail.yaml",
            "has-comment/lqllint.json",
            "Change set must have a comment");

        IntegrationTestConfig test6 = IntegrationTestConfig.shouldPass(
            "Should pass with a comment yaml",
            "has-comment/has-comment-pass.yaml",
            "has-comment/lqllint.json");

        return Arrays.asList(test1, test2, test3, test4, test5, test6);
    }

}
