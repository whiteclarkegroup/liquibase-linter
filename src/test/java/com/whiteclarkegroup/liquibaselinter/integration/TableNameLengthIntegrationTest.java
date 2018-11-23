package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class TableNameLengthIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when table name length is exceeded",
            "table-name-length/table-name-length-fail.xml",
            "table-name-length/lqllint.json",
            "Table 'THIS_TABLE_NAME_IS_FAR_TOO_LONG' name must not be longer than 26");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when table name length is not exceeded",
            "table-name-length/table-name-length-pass.xml",
            "table-name-length/lqllint.json");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldFail(
            "Should fail when table name length is exceeded",
            "table-name-length/table-name-length-rename-fail.xml",
            "table-name-length/lqllint.json",
            "Table 'THIS_TABLE_NAME_IS_FAR_TOO_LONG' name must not be longer than 26");

        return Arrays.asList(test1, test2, test3);
    }

}
