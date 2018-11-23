package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class CreateColumnNoDefinePrimaryKeyIntegrationTest extends LinterIntegrationTest {

    @Override
    List<IntegrationTestConfig> getTests() {
        IntegrationTestConfig test1 = IntegrationTestConfig.shouldFail(
            "Should fail when create column specifies primary key constraint attribute",
            "create-column-no-define-primary-key/create-column-no-define-primary-key-fail.xml",
            "create-column-no-define-primary-key/lqllint.json",
            "Add column must not use primary key attribute. Instead use AddPrimaryKey change type");

        IntegrationTestConfig test2 = IntegrationTestConfig.shouldPass(
            "Should pass when create column does not specifies primary key constraint attribute",
            "create-column-no-define-primary-key/create-column-no-define-primary-key-pass.xml",
            "create-column-no-define-primary-key/lqllint.json");

        IntegrationTestConfig test3 = IntegrationTestConfig.shouldFail(
            "Should fail when create column specifies primary key constraint attribute",
            "create-column-no-define-primary-key/create-column-no-define-primary-key-create-table-fail.xml",
            "create-column-no-define-primary-key/lqllint.json",
            "Add column must not use primary key attribute. Instead use AddPrimaryKey change type");

        return Arrays.asList(test1, test2, test3);
    }

}
