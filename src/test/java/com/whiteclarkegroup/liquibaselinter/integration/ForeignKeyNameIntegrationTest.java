package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ForeignKeyNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when the contextual naming can't be enforced but the suffix isn't used",
            "foreign-key-name/foreign-key-name-fail-on-suffix.xml",
            "foreign-key-name/foreign-key-name-complex.json",
            "Foreign key constraint 'NOT_EVEN_CLOSE' must be named, ending in _FK, and follow pattern '{base_table_name}_{parent_table_name}_FK' where space permits");

        shouldFail(
            "Should fail when the contextual naming can be enforced and isn't used",
            "foreign-key-name/foreign-key-name-fail-on-convention.xml",
            "foreign-key-name/foreign-key-name-complex.json",
            "Foreign key constraint 'WHOOPS_FK' must be named, ending in _FK, and follow pattern '{base_table_name}_{parent_table_name}_FK' where space permits");

        shouldPass(
            "Should pass when used correctly",
            "foreign-key-name/foreign-key-name-pass.xml",
            "foreign-key-name/foreign-key-name-complex.json");
    }

}
