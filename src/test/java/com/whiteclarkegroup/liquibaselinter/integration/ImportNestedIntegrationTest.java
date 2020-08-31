package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ImportNestedIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when object name characters are invalid",
            "import-nested/import-nested-fail-chars.xml",
            "import-nested/lqlint.json",
            "Object name 'OBJ_COLUMN1' name must be uppercase letters and use '_' separation");

        shouldFail(
            "Should fail when object name prefix invalid",
            "import-nested/import-nested-fail-prefix.xml",
            "import-nested/lqlint.json",
            "Object name 'COLUMN' name must start with 'OBJ_'");

        shouldPass(
            "Should pass all imported rules",
            "import-nested/import-nested-pass.xml",
            "import-nested/lqlint.json");
    }

}
