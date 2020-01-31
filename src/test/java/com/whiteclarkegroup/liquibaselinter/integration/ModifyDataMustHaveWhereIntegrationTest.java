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
            "modify-data-enforce-where/lqlint.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition");

        shouldPass(
            "Should pass when modify data has where condition",
            "modify-data-enforce-where/modify-data-enforce-where-pass.xml",
            "modify-data-enforce-where/lqlint.json");

        shouldFail(
            "Should fail when delete data does not have where condition",
            "modify-data-enforce-where/modify-data-enforce-where-fail-delete.xml",
            "modify-data-enforce-where/lqlint.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition");

        shouldPass(
            "Should pass when modify data doesnt require where condition",
            "modify-data-enforce-where/modify-data-enforce-where-pass-other-table.xml",
            "modify-data-enforce-where/lqlint.json");

        shouldFail(
            "Should fail when modify data where condition does not match pattern",
            "modify-data-enforce-where/modify-data-enforce-where-fail-pattern-mismatch.xml",
            "modify-data-enforce-where/lqlint-pattern.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition including with 'CODE = '");

        shouldFail(
            "Should fail when modify data where condition missing when pattern provided",
            "modify-data-enforce-where/modify-data-enforce-where-fail-pattern-missing.xml",
            "modify-data-enforce-where/lqlint-pattern.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition including with 'CODE = '");

        shouldPass(
            "Should pass when modify data has where condition matching pattern",
            "modify-data-enforce-where/modify-data-enforce-where-pass-pattern.xml",
            "modify-data-enforce-where/lqlint-pattern.json");

        shouldFail(
            "Should fail when delete data does not have where condition with regex matcher",
            "modify-data-enforce-where/modify-data-enforce-where-fail-delete.xml",
            "modify-data-enforce-where/lqlint-regex.json",
            "Modify data on table 'MUST_HAVE_WHERE' must have a where condition");

    }

}
