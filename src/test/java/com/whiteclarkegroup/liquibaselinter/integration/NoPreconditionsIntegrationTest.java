package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class NoPreconditionsIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should not allow preconditions at changeset level",
            "no-preconditions/no-preconditions-changeset.xml",
            "no-preconditions/no-preconditions.json",
            "Preconditions are not allowed in this project");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldFail(
            "Should not allow preconditions at changelog level",
            "no-preconditions/no-preconditions-changelog.xml",
            "no-preconditions/no-preconditions.json",
            "Preconditions are not allowed in this project");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldPass(
            "Should pass without any preconditions",
            "no-preconditions/no-preconditions-pass.xml",
            "no-preconditions/no-preconditions.json");

        return Arrays.asList(test1, test2, test3);
    }

}
