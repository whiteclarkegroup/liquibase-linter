package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class TableNameIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when table name does not match the pattern",
            "table-name/table-name-fail.xml",
            "table-name/lqllint.json",
            "Table 'TBL_TEST' name must be uppercase, use '_' separation and not start with TBL");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when table name matches the pattern",
            "table-name/table-name-pass.xml",
            "table-name/lqllint.json");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldFail(
            "Should fail when table name does not match the pattern",
            "table-name/table-name-rename-fail.xml",
            "table-name/lqllint.json",
            "Table 'TBL_TEST' name must be uppercase, use '_' separation and not start with TBL");

        return Arrays.asList(test1, test2, test3);
    }

}
