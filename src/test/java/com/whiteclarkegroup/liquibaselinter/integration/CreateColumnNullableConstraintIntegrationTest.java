package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class CreateColumnNullableConstraintIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when create column does not have a constraints tag with a populated nullable attribute",
            "create-column-nullable-constraint/create-column-nullable-constraint-fail.xml",
            "create-column-nullable-constraint/lqllint.json",
            "Add column must specify nullable constraint");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when create column has a constraints tag with a populated nullable attribute",
            "create-column-nullable-constraint/create-column-nullable-constraint-pass.xml",
            "create-column-nullable-constraint/lqllint.json");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldFail(
            "Should fail when create table column does not have a constraints tag with a populated nullable attribute",
            "create-column-nullable-constraint/create-column-nullable-constraint-fail-create-table.xml",
            "create-column-nullable-constraint/lqllint.json",
            "Add column must specify nullable constraint");

        return Arrays.asList(test1, test2, test3);
    }

}
