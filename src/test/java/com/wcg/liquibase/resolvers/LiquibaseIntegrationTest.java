package com.wcg.liquibase.resolvers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LiquibaseIntegrationTest {

    String changeLogFile();

    String configFile();

}
