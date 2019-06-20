package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class NoSchemaNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when schema name is present",
            "no-schema-name/no-schema-name-fail.xml",
            "no-schema-name/no-schema-name.json",
            "Schema names are not allowed in this project");

        shouldPass(
            "Should pass when schema name is not present",
            "no-schema-name/no-schema-name-pass.xml",
            "no-schema-name/no-schema-name.json");
    }

}
