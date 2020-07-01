package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class AllowableXsdVersionRuleIntegrationTest extends LinterIntegrationTest {


    @Override
    void registerTests() {
        shouldPass(
            "Should allow XSD version 3.4",
            "allowable-xsd-version/allowable-xsd-version-valid.xml",
            "allowable-xsd-version/allowable-xsd-version.json");

        shouldFail(
            "Should not allow XSD version 3.3",
            "allowable-xsd-version/allowable-xsd-version-invalid.xml",
            "allowable-xsd-version/allowable-xsd-version.json",
            "src/test/resources/integration/allowable-xsd-version/allowable-xsd-version-invalid.xml -- Message: Changelog should use an XSD version in [3.4, 3.5]");
    }

}
