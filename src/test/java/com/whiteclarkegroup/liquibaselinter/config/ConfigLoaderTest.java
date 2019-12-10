package com.whiteclarkegroup.liquibaselinter.config;

import com.google.common.collect.ImmutableSet;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static com.whiteclarkegroup.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG;
import static com.whiteclarkegroup.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG_PATH_PROPERTY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigLoaderTest {

    private ConfigLoader configLoader;

    @AfterAll
    public static void teardown() {
        System.clearProperty(LQLINT_CONFIG_PATH_PROPERTY);
    }

    @BeforeEach
    void setUp() {
        configLoader = new ConfigLoader();
    }

    @DisplayName("Should load from system property is present")
    @Test
    void shouldLoadFromSystemPropertyFirst() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        String customPath = "/test-lqlint.json";
        System.setProperty(LQLINT_CONFIG_PATH_PROPERTY, customPath);
        when(resourceAccessor.getResourcesAsStream(customPath)).thenReturn(ImmutableSet.of(getInputStream()));
        when(resourceAccessor.getResourcesAsStream(LQLINT_CONFIG)).thenReturn(ImmutableSet.of(getInputStream()));
        Config config = configLoader.load(resourceAccessor);
        assertNotNull(config);
        verify(resourceAccessor, times(0)).getResourcesAsStream(LQLINT_CONFIG);
    }

    @DisplayName("Should throw if cannot load config")
    @Test
    void shouldThrowIfCannotLoadConfig() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.getResourcesAsStream(LQLINT_CONFIG)).thenReturn(Collections.emptySet());

        UnexpectedLiquibaseException unexpectedLiquibaseException =
            assertThrows(UnexpectedLiquibaseException.class, () -> configLoader.load(resourceAccessor));

        assertTrue(unexpectedLiquibaseException.getMessage().contains("Failed to load lq lint config file"));
    }

    @DisplayName("Should throw on io exception")
    @Test
    void shouldThrowOnIoException() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.getResourcesAsStream(LQLINT_CONFIG)).thenThrow(new IOException());

        UnexpectedLiquibaseException unexpectedLiquibaseException =
            assertThrows(UnexpectedLiquibaseException.class, () -> configLoader.load(resourceAccessor));

        assertTrue(unexpectedLiquibaseException.getMessage().contains("Failed to load lq lint config file"));
    }

    private InputStream getInputStream() {
        return getClass().getResourceAsStream("/lqlint.test.json");
    }

}
