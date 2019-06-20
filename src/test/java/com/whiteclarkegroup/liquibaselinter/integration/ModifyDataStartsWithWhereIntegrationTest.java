package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ModifyDataStartsWithWhereIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when modify data where condition starts with 'where'",
            "modify-data-starts-with-where/modify-data-starts-with-where-fail.xml",
            "modify-data-starts-with-where/lqllint.json",
            "Modify data where starts with where clause, that's probably a mistake");

        shouldPass(
            "Should pass when modify data where condition does not start with 'where'",
            "modify-data-starts-with-where/modify-data-starts-with-where-pass.xml",
            "modify-data-starts-with-where/lqllint.json");
    }

}
