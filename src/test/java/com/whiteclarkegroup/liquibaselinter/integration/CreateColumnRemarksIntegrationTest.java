package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class CreateColumnRemarksIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when create column does not have populated remarks attribute",
            "create-column-remarks/create-column-remarks-fail.xml",
            "create-column-remarks/lqllint.json",
            "Add column must contain remarks");

        shouldPass(
            "Should pass when create column has populated remarks attribute",
            "create-column-remarks/create-column-remarks-pass.xml",
            "create-column-remarks/lqllint.json");

        shouldFail(
            "Should fail when create column does not have populated remarks attribute",
            "create-column-remarks/create-column-remarks-create-table-fail.xml",
            "create-column-remarks/lqllint.json",
            "Add column must contain remarks");
    }

}
