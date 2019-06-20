package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class NoPreconditionsIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should not allow preconditions at changeset level",
            "no-preconditions/no-preconditions-changeset.xml",
            "no-preconditions/no-preconditions.json",
            "Preconditions are not allowed in this project");

        shouldFail(
            "Should not allow preconditions at changelog level",
            "no-preconditions/no-preconditions-changelog.xml",
            "no-preconditions/no-preconditions.json",
            "Preconditions are not allowed in this project");

        shouldPass(
            "Should pass without any preconditions",
            "no-preconditions/no-preconditions-pass.xml",
            "no-preconditions/no-preconditions.json");
    }

}
