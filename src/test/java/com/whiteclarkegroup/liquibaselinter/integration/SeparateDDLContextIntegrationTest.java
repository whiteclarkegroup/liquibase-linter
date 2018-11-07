package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class SeparateDDLContextIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail with ddl script under non ddl context",
            "separate-ddl-context/separate-ddl-context-fail.xml",
            "separate-ddl-context/lqllint.json",
            "Should have a ddl changes under ddl contexts");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass ddl script under ddl context",
            "separate-ddl-context/separate-ddl-context-pass.xml",
            "separate-ddl-context/lqllint.json");

        return Arrays.asList(test1, test2);
    }

}
