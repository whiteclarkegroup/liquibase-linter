package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class CreateColumnRemarksIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when create column does not have populated remarks attribute",
            "create-column-remarks/create-column-remarks-fail.xml",
            "create-column-remarks/lqllint.json",
            "Add column must contain remarks");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when create column has populated remarks attribute",
            "create-column-remarks/create-column-remarks-pass.xml",
            "create-column-remarks/lqllint.json");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldFail(
            "Should fail when create column does not have populated remarks attribute",
            "create-column-remarks/create-column-remarks-create-table-fail.xml",
            "create-column-remarks/lqllint.json",
            "Add column must contain remarks");

        return Arrays.asList(test1, test2, test3);
    }

}
