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
            "Should fail with no comment",
            "has-comment/has-comment-fail.xml",
            "has-comment/lqllint.json",
            "Change set must have a comment");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass with a comment",
            "has-comment/has-comment-pass.xml",
            "has-comment/lqllint.json");

        return Arrays.asList(test1, test2);
    }

}
