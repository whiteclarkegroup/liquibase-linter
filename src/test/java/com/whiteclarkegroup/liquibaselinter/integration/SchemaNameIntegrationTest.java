package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class SchemaNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when schema name doesn't match pattern",
            "schema-name/schema-name-fail.xml",
            "schema-name/schema-name.json",
            "Must use schema name token, not SCHEMA_NAME");

        shouldPass(
            "Should pass when schema name matches pattern",
            "schema-name/schema-name-pass.xml",
            "schema-name/schema-name.json");
    }

}
