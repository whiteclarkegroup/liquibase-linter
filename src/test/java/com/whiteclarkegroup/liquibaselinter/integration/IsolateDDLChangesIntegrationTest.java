package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class IsolateDDLChangesIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with more than one ddl change within a single change set",
            "isolate-ddl-changes/isolate-ddl-changes-fail.xml",
            "isolate-ddl-changes/lqllint.json",
            "Should only have a single ddl change per change set");

        shouldPass(
            "Should pass with a single ddl change within a single change set",
            "isolate-ddl-changes/isolate-ddl-changes-pass.xml",
            "isolate-ddl-changes/lqllint.json");
    }

}
