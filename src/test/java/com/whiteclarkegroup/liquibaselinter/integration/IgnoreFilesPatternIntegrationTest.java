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

        shouldPass(
            "Should ignore file with spaces when in ignore pattern",
            "ignore-files-pattern/nested/ignore/ignore change log with spaces.xml",
            "ignore-files-pattern/lqlint-ignore-spaces.json");

        shouldFail(
            "Should fail when not in ignore pattern",
            "ignore-files-pattern/nested/ignore/do not ignore spaces.xml",
            "ignore-files-pattern/lqlint-ignore-spaces.json",
            "Change set must have a comment");
    }

}
