package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class MatchesContextIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "should match simple context and fail rule",
            "matches-context/simple-context-foo.xml",
            "matches-context/simple-context-foo.json",
            "Change set must have a comment");

        shouldPass(
            "should not match negative simple context and pass rule",
            "matches-context/simple-context-not-foo.xml",
            "matches-context/simple-context-foo.json");

        shouldPass(
            "should not match simple context mismatch and pass rule",
            "matches-context/simple-context-foo.xml",
            "matches-context/simple-context-bar.json");

        shouldPass(
            "should not match no context and pass rule",
            "matches-context/no-context.xml",
            "matches-context/simple-context-foo.json");

        shouldFail(
            "should match multiple contexts and fail rule",
            "matches-context/multiple-context-foo-bar.xml",
            "matches-context/multiple-context-foo-bar.json",
            "Change set must have a comment");

        shouldFail(
            "should match multiple contexts (with not) and fail rule",
            "matches-context/multiple-context-foo-not-bar.xml",
            "matches-context/multiple-context-foo-not-bar.json",
            "Change set must have a comment");
    }

}
