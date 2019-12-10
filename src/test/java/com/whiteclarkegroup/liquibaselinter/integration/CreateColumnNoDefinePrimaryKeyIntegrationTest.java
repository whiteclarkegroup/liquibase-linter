package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class CreateColumnNoDefinePrimaryKeyIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when create column specifies primary key constraint attribute",
            "create-column-no-define-primary-key/create-column-no-define-primary-key-fail.xml",
            "create-column-no-define-primary-key/lqlint.json",
            "Add column must not use primary key attribute. Instead use AddPrimaryKey change type");

        shouldPass(
            "Should pass when create column does not specifies primary key constraint attribute",
            "create-column-no-define-primary-key/create-column-no-define-primary-key-pass.xml",
            "create-column-no-define-primary-key/lqlint.json");

        shouldFail(
            "Should fail when create column specifies primary key constraint attribute",
            "create-column-no-define-primary-key/create-column-no-define-primary-key-create-table-fail.xml",
            "create-column-no-define-primary-key/lqlint.json",
            "Add column must not use primary key attribute. Instead use AddPrimaryKey change type");
    }

}
