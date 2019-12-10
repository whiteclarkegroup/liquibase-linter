package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class TableNameLengthIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when table name length is exceeded",
            "table-name-length/table-name-length-fail.xml",
            "table-name-length/lqlint.json",
            "Table 'THIS_TABLE_NAME_IS_FAR_TOO_LONG' name must not be longer than 26");

        shouldPass(
            "Should pass when table name length is not exceeded",
            "table-name-length/table-name-length-pass.xml",
            "table-name-length/lqlint.json");

        shouldFail(
            "Should fail when table name length is exceeded",
            "table-name-length/table-name-length-rename-fail.xml",
            "table-name-length/lqlint.json",
            "Table 'THIS_TABLE_NAME_IS_FAR_TOO_LONG' name must not be longer than 26");
    }

}
