package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ModifyDataMustHaveWhereIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when modify data does not have where condition",
            "modify-data-enforce-where/modify-data-enforce-where-fail.xml",
            "modify-data-enforce-where/lqllint.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition");

        shouldPass(
            "Should pass when modify data has where condition",
            "modify-data-enforce-where/modify-data-enforce-where-pass.xml",
            "modify-data-enforce-where/lqllint.json");

        shouldFail(
            "Should fail when delete data does not have where condition",
            "modify-data-enforce-where/modify-data-enforce-where-fail-delete.xml",
            "modify-data-enforce-where/lqllint.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition");

        shouldPass(
            "Should pass when modify data doesnt require where condition",
            "modify-data-enforce-where/modify-data-enforce-where-pass-other-table.xml",
            "modify-data-enforce-where/lqllint.json");
    }

}
