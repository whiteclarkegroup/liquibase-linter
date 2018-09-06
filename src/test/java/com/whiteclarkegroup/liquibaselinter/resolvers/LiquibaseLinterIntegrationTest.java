package com.whiteclarkegroup.liquibaselinter.resolvers;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Test
@Retention(RetentionPolicy.RUNTIME)
public @interface LiquibaseLinterIntegrationTest {

    String changeLogFile();

    String configFile();

}
