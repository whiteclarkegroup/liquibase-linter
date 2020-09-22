package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ImportMultipleIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when object name characters are invalid",
            "import-multi/import-multi-fail-chars.xml",
            "import-multi/lqlint.json",
            "Object name 'OBJ_COLUMN1' name must be uppercase letters and use '_' separation");

        shouldFail(
            "Should fail when object name prefix invalid",
            "import-multi/import-multi-fail-prefix.xml",
            "import-multi/lqlint.json",
            "Object name 'COLUMN' name must start with 'OBJ_'");

        shouldPass(
            "Should pass all imported rules",
            "import-multi/import-multi-pass.xml",
            "import-multi/lqlint.json");
    }

}
