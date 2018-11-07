package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class CreateTableRemarksIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when create table does not have populated remarks attribute",
            "create-table-remarks/create-table-remarks-fail.xml",
            "create-table-remarks/lqllint.json",
            "Create table must contain remark attribute");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when create table has populated remarks attribute",
            "create-table-remarks/create-table-remarks-pass.xml",
            "create-table-remarks/lqllint.json");

        return Arrays.asList(test1, test2);
    }

}
