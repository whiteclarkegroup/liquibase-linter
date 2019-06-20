package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class TableNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when table name does not match the pattern",
            "table-name/table-name-fail.xml",
            "table-name/lqllint.json",
            "Table 'TBL_TEST' name must be uppercase, use '_' separation and not start with TBL");

        shouldPass(
            "Should pass when table name matches the pattern",
            "table-name/table-name-pass.xml",
            "table-name/lqllint.json");

        shouldFail(
            "Should fail when table name does not match the pattern",
            "table-name/table-name-rename-fail.xml",
            "table-name/lqllint.json",
            "Table 'TBL_TEST' name must be uppercase, use '_' separation and not start with TBL");
    }

}
