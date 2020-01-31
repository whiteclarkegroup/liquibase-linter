package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class NoRawSqlIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should not raw sql change type",
            "no-raw-sql/raw-sql-change-type.xml",
            "no-raw-sql/no-raw-sql.json",
            "Raw sql change types are not allowed, use appropriate Liquibase change types");

        shouldFail(
            "Should not sql file change type",
            "no-raw-sql/sql-file-change-type.xml",
            "no-raw-sql/no-raw-sql.json",
            "Raw sql change types are not allowed, use appropriate Liquibase change types");

        shouldPass(
            "Should allow non raw sql change type",
            "no-raw-sql/non-raw-sql-change-type.xml",
            "no-raw-sql/no-raw-sql.json");
    }

}
