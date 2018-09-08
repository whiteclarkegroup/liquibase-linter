package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class PrimaryKeyNameIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
                "Should fail when the table name can't be enforced but the suffix isn't used",
                "primary-key-name-fail-on-suffix.xml",
                "primary-key-name.json",
                "Primary key constraint 'NOT_EVEN_CLOSE' must end with '_PK', and start with table name (unless too long)");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldFail(
                "Should fail when the table name can be enforced and isn't used",
                "primary-key-name-fail-on-tablename.xml",
                "primary-key-name.json",
                "Primary key constraint 'BAZ_PK' must end with '_PK', and start with table name (unless too long)");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldPass(
            "Should pass when used correctly",
            "primary-key-name-pass.xml",
            "primary-key-name.json");

        return Arrays.asList(test1, test2, test3);
    }

}
