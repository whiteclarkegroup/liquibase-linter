package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ModifyDataMustHaveWhereIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when modify data does not have where condition",
            "modify-data-enforce-where/modify-data-enforce-where-fail.xml",
            "modify-data-enforce-where/lqllint.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when modify data has where condition",
            "modify-data-enforce-where/modify-data-enforce-where-pass.xml",
            "modify-data-enforce-where/lqllint.json");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldFail(
            "Should fail when delete data does not have where condition",
            "modify-data-enforce-where/modify-data-enforce-where-fail-delete.xml",
            "modify-data-enforce-where/lqllint.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition");

        IntegrationTestConfig test4 = IntegrationTestConfig.shouldPass(
            "Should pass when modify data doesnt require where condition",
            "modify-data-enforce-where/modify-data-enforce-where-pass-other-table.xml",
            "modify-data-enforce-where/lqllint.json");

        IntegrationTestConfig test5 = IntegrationTestConfig.shouldFail(
            "Should fail when modify data where condition does not match pattern",
            "modify-data-enforce-where/modify-data-enforce-where-fail-pattern-mismatch.xml",
            "modify-data-enforce-where/lqllint-pattern.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition including with 'CODE = '");

        IntegrationTestConfig test6 = IntegrationTestConfig.shouldFail(
            "Should fail when modify data where condition missing when pattern provided",
            "modify-data-enforce-where/modify-data-enforce-where-fail-pattern-missing.xml",
            "modify-data-enforce-where/lqllint-pattern.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition including with 'CODE = '");

        IntegrationTestConfig test7 = IntegrationTestConfig.shouldPass(
            "Should pass when modify data has where condition matching pattern",
            "modify-data-enforce-where/modify-data-enforce-where-pass-pattern.xml",
            "modify-data-enforce-where/lqllint-pattern.json");

        return Arrays.asList(test1, test2, test3, test4, test5, test6, test7);
    }

}
