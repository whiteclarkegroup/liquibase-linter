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
            "no-preconditions-changeset.xml",
            "no-preconditions.json",
            "Preconditions are not allowed in this project");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldFail(
            "Should not allow preconditions at changelog level",
            "no-preconditions-changelog.xml",
            "no-preconditions.json",
            "Preconditions are not allowed in this project");

        return Arrays.asList(test1, test2);
    }

}
