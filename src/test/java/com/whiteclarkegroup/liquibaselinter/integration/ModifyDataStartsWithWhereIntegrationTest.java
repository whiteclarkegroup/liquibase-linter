package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ModifyDataStartsWithWhereIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when modify data where condition starts with 'where'",
            "modify-data-starts-with-where/modify-data-starts-with-where-fail.xml",
            "modify-data-starts-with-where/lqllint.json",
            "Modify data where starts with where clause, that's probably a mistake");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when modify data where condition does not start with 'where'",
            "modify-data-starts-with-where/modify-data-starts-with-where-pass.xml",
            "modify-data-starts-with-where/lqllint.json");

        return Arrays.asList(test1, test2);
    }

}
