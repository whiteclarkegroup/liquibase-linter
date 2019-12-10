package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class SeparateDDLContextIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with ddl script under non ddl context",
            "separate-ddl-context/separate-ddl-context-fail.xml",
            "separate-ddl-context/lqlint.json",
            "Should have a ddl changes under ddl contexts");

        shouldPass(
            "Should pass ddl script under ddl context",
            "separate-ddl-context/separate-ddl-context-pass.xml",
            "separate-ddl-context/lqlint.json");
    }

}
