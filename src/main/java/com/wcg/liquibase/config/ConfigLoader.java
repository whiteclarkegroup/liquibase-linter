package com.wcg.liquibase.config;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class ConfigLoader {

    public static final String LQLLINT_CONFIG = "/lqllint.json";
    private static final String LQLLINT_DEFAULT_CONFIG = "/lqllint.default.json";
    private final Config DEFAULT_CONFIG;

    public ConfigLoader() {
        DEFAULT_CONFIG = loadDefaults();
    }

    public Config load(ResourceAccessor resourceAccessor) {
        return DEFAULT_CONFIG.mixin(loadOverrides(resourceAccessor));
    }

    private Config loadDefaults() {
        try (InputStream inputStream = getClass().getResourceAsStream(LQLLINT_DEFAULT_CONFIG)) {
            return Config.fromInputStream(inputStream);
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException("Failed to load lq lint default config file", e);
        }
    }

    private Config loadOverrides(ResourceAccessor resourceAccessor) {
        try {
            Set<InputStream> resourcesAsStream = resourceAccessor.getResourcesAsStream(LQLLINT_CONFIG);
            if (resourcesAsStream != null && !resourcesAsStream.isEmpty()) {
                try (InputStream inputStream = resourcesAsStream.iterator().next()) {
                    return Config.fromInputStream(inputStream);
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

}
