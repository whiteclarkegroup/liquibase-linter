package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ObjectNameLengthIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when object name length is exceeded",
            "object-name-length/object-name-length-fail.xml",
            "object-name-length/lqllint.json",
            "Object name 'THIS_OBJECT_NAME_IS_FAR_TOO_LONG' must be less than 30 characters");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when object name length is not exceeded",
            "object-name-length/object-name-length-pass.xml",
            "object-name-length/lqllint.json");

        return Arrays.asList(test1, test2);
    }

}
