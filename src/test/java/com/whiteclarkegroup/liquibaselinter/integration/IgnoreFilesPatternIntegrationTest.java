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
            "ignore-files-pattern/lqlint.json",
            "Change set must have a comment");

        shouldPass(
            "Should ignore file with no comment xml",
            "ignore-files-pattern/subdir/has-comment-fail.xml",
            "ignore-files-pattern/lqlint-ignore.json");

        shouldFail(
            "Should fail with no comment xml nested",
            "ignore-files-pattern/nested/root-change-log.xml",
            "ignore-files-pattern/lqlint.json",
            "Change set must have a comment");

        shouldPass(
            "Should ignore file with no comment xml nested",
            "ignore-files-pattern/nested/root-change-log.xml",
            "ignore-files-pattern/lqlint-ignore-nested.json");
    }

}
