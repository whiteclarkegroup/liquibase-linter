package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class IgnoreFilesPatternIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with no comment xml",
            "ignore-files-pattern/subdir/has-comment-fail.xml",
            "ignore-files-pattern/lqllint.json",
            "Change set must have a comment");

        shouldPass(
            "Should ignore file with no comment xml",
            "ignore-files-pattern/subdir/has-comment-fail.xml",
            "ignore-files-pattern/lqllint-ignore.json");
    }

}
