package com.whiteclarkegroup.liquibaselinter.config;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class ConfigLoader {

    public static final String LQLINT_CONFIG = "/lqlint.json";
    public static final String LQLLINT_CONFIG = "/lqllint.json";

    public Config load(ResourceAccessor resourceAccessor) {
        try {
            Config config = loadConfig(resourceAccessor, LQLINT_CONFIG);
            if (config != null) {
                return config;
            } else {
                config = loadConfig(resourceAccessor, LQLLINT_CONFIG);
                if (config != null) {
                    return config;
                }
            }
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException("Failed to load lq lint config file", e);
        }
        throw new UnexpectedLiquibaseException("Failed to load lq lint config file");
    }

    private Config loadConfig(ResourceAccessor resourceAccessor, String path) throws IOException {
        final Set<InputStream> resourcesAsStream = resourceAccessor.getResourcesAsStream(path);
        if (resourcesAsStream != null && !resourcesAsStream.isEmpty()) {
            try (InputStream inputStream = resourcesAsStream.iterator().next()) {
                return Config.fromInputStream(inputStream);
            }
        }
        return null;
    }

}
