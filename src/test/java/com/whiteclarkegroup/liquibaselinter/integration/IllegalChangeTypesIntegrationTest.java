package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class IllegalChangeTypesIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should not allow a illegal change type",
            "illegal-change-types/illegal-change-types.xml",
            "illegal-change-types/illegal-change-types.json",
            "Change type 'liquibase.change.core.LoadDataChange' is not allowed in this project");

        shouldFail(
            "Should not allow a illegal change type simple",
            "illegal-change-types/illegal-change-types.xml",
            "illegal-change-types/illegal-change-types-simple.json",
            "Change type 'liquibase.change.core.LoadDataChange' is not allowed in this project");
    }

}
