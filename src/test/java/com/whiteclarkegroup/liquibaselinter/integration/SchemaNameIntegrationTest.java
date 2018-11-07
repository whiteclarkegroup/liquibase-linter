package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class SchemaNameIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when schema name doesn't match pattern",
            "schema-name/schema-name-fail.xml",
            "schema-name/schema-name.json",
            "Must use schema name token, not SCHEMA_NAME");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when schema name matches pattern",
            "schema-name/schema-name-pass.xml",
            "schema-name/schema-name.json");

        return Arrays.asList(test1, test2);
    }

}
