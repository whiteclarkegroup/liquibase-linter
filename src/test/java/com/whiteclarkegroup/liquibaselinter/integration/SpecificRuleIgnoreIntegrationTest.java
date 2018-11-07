package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class SpecificRuleIgnoreIntegrationTest extends LinterIntegrationTest {
    // TODO test in more detail, proving that it still fails on other non-ignored rules, once #20 is fixed
    
    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldPass(
                "Should be allowed to ignore specific rules",
            "specific-rule-ignore/specific-rule-ignore.xml",
            "specific-rule-ignore/specific-rule-ignore.json");

        return Collections.singletonList(test1);
    }

}
