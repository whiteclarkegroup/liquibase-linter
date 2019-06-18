package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class FileNotIncludedIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should not allow file not included in deltas change log",
            "file-not-included/file-not-included-root.xml",
            "file-not-included/file-not-included.json",
            "Changelog file 'src/test/resources/integration/file-not-included/to-include/test-included-2.xml' was not included in deltas change log");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should allow all files included in deltas change log",
            "file-not-included/files-included-root.xml",
            "file-not-included/file-not-included.json");
        return Arrays.asList(test1, test2);
    }

}
