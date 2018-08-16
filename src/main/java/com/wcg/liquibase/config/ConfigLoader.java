package com.wcg.liquibase.config;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class ConfigLoader {

    public Config load(ResourceAccessor resourceAccessor) {
        return loadDefaults().mixin(loadOverrides(resourceAccessor));
    }

    private Config loadDefaults() {
        try (InputStream inputStream = getClass().getResourceAsStream("/lqllint.default.json")) {
            return Config.fromInputStream(inputStream);
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException("Failed to load lq lint default config file", e);
        }
    }

    private Config loadOverrides(ResourceAccessor resourceAccessor) {
        try {
            Set<InputStream> resourcesAsStream = resourceAccessor.getResourcesAsStream("/lqllint.json");
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
