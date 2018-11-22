package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class NoSchemaNameIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when schema name is present",
            "no-schema-name/no-schema-name-fail.xml",
            "no-schema-name/no-schema-name.json",
            "Schema names are not allowed in this project");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when schema name is not present",
            "no-schema-name/no-schema-name-pass.xml",
            "no-schema-name/no-schema-name.json");

        return Arrays.asList(test1, test2);
    }

}
