package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class CreateIndexNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when create index name does not match the pattern",
            "create-index-name/create-index-name-fail.xml",
            "create-index-name/lqlint.json",
            "Index 'TABLE_NAME' must follow pattern table name followed by 'I' and a number e.g. APPLICATION_I1, or match a primary key or unique constraint name");

        shouldPass(
            "Should pass when create index name matches the pattern",
            "create-index-name/create-index-name-pass.xml",
            "create-index-name/lqlint.json");

        shouldPass(
            "Should pass when create index name does not match the pattern but the condition is not met",
            "create-index-name/create-index-name-pass-condition-not-met.xml",
            "create-index-name/lqlint.json");
    }

}
