package com.wcg.liquibase.config;

import liquibase.resource.FileSystemResourceAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigLoaderTest {

    private ConfigLoader configLoader;

    @BeforeEach
    void setUp() {
        configLoader = new ConfigLoader();
    }

    @Test
    void should_load_defaults() {
        Config config = configLoader.load(new FileSystemResourceAccessor());
        assertNotNull(config);
    }

}
