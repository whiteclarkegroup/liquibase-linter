package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class NoReservedWordsIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when a reserved word is used as an object name",
            "no-reserved-words/no-reserved-words-fail.xml",
            "no-reserved-words/lqllint.json",
            "'SESSION_USER' is a reserved word; choose a different name");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when something valid is used as an object name",
            "no-reserved-words/no-reserved-words-pass.xml",
            "no-reserved-words/lqllint.json");

        return Arrays.asList(test1, test2);
    }

}
