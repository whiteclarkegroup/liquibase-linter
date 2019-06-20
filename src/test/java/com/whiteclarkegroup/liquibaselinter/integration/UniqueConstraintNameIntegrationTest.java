package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class UniqueConstraintNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when unique constraint name does not match the pattern",
            "unique-constraint-name/unique-constraint-name-fail.xml",
            "unique-constraint-name/lqllint.json",
            "Unique constraint 'TABLE_NAME' must follow pattern table name followed by 'U' and a number e.g. TABLE_U1");

        shouldPass(
            "Should pass when unique constraint name matches the pattern",
            "unique-constraint-name/unique-constraint-name-pass.xml",
            "unique-constraint-name/lqllint.json");

        shouldPass(
            "Should pass when unique constraint name does not match the pattern but the condition is not met",
            "unique-constraint-name/unique-constraint-name-pass-condition-not-met.xml",
            "unique-constraint-name/lqllint.json");
    }

}
