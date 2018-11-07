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
            "primary-key-name/primary-key-name-fail-on-suffix.xml",
            "primary-key-name/primary-key-name-complex.json",
                "Primary key constraint 'NOT_EVEN_CLOSE' must be named, ending with '_PK', and start with table name (unless too long)");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldFail(
                "Should fail when the table name can be enforced and isn't used",
            "primary-key-name/primary-key-name-fail-on-tablename.xml",
            "primary-key-name/primary-key-name-complex.json",
                "Primary key constraint 'BAZ_PK' must be named, ending with '_PK', and start with table name (unless too long)");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldPass(
            "Should pass when used correctly",
            "primary-key-name/primary-key-name-pass.xml",
            "primary-key-name/primary-key-name-complex.json");

        IntegrationTestConfig test4 = IntegrationTestConfig.shouldFail(
            "Should fail when omitted with simple config",
            "primary-key-name/primary-key-name-fail-omitted.xml",
            "primary-key-name/primary-key-name-simple.json",
            "Primary key name is missing or does not follow pattern");

        IntegrationTestConfig test5 = IntegrationTestConfig.shouldFail(
            "Should fail when omitted with complex config",
            "primary-key-name/primary-key-name-fail-omitted.xml",
            "primary-key-name/primary-key-name-complex.json",
            "Primary key constraint '' must be named, ending with '_PK', and start with table name (unless too long)");

        return Arrays.asList(test1, test2, test3, test4, test5);
    }

}
