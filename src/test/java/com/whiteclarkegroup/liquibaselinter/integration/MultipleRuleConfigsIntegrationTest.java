package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class MultipleRuleConfigsIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail on the uppercase etc pattern",
            "multiple-rule-configs/fail-first.xml",
            "multiple-rule-configs/lqllint.json",
            "Object name 'nope' name must be uppercase and use '_' separation");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldFail(
            "Should fail on the prefix pattern",
            "multiple-rule-configs/fail-second.xml",
            "multiple-rule-configs/lqllint.json",
            "Object name 'NOPE' name must begin with 'POWER'");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldPass(
            "Should pass with mutiple configs if they all pass",
            "multiple-rule-configs/pass.xml",
            "multiple-rule-configs/lqllint.json");

        return Arrays.asList(test1, test2, test3);
    }

}
