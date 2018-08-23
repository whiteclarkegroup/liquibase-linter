package com.wcg.liquibase.resolvers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LiquibaseLinterIntegrationTest {

    String changeLogFile();

    String configFile();

}
