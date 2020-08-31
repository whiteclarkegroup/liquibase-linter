package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class DuplicateIncludesIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should not allow duplicate file includes",
            "duplicate-includes/duplicate-includes.xml",
            "duplicate-includes/duplicate-includes.json",
            "liquibase.exception.SetupException: Changelog file 'integration/duplicate-includes/duplicate-includes-change.xml' was included more than once");

        shouldFail(
            "Should not allow duplicate file includes nested",
            "duplicate-includes/duplicate-includes-nested.xml",
            "duplicate-includes/duplicate-includes.json",
            "liquibase.exception.SetupException: Changelog file 'integration/duplicate-includes/duplicate-includes-change.xml' was included more than once");

        shouldPass(
            "Should not mark as duplicate include when the file contains no change sets",
            "duplicate-includes/duplicate-includes-no-change-set.xml",
            "duplicate-includes/duplicate-includes.json");
    }

}
