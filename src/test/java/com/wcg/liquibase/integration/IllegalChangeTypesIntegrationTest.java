package com.wcg.liquibase.integration;

import com.wcg.liquibase.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class IllegalChangeTypesIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
                "Should not allow a illegal change type",
                "illegal-change-types.xml",
                "illegal-change-types.json",
                "Change type 'LoadDataChange' is not allowed in this project");

        return Collections.singletonList(test1);
    }

}
