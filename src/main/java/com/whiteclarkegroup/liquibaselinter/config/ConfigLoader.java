package com.whiteclarkegroup.liquibaselinter.config;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

public class ConfigLoader {

    public static final String LQLINT_CONFIG = "/lqlint.json";
    public static final String LQLINT_CONFIG_PATH_PROPERTY = "lqlint.config.path";

    public Config load(ResourceAccessor resourceAccessor) {
        try {
            String configPath = Optional.ofNullable(System.getProperty(LQLINT_CONFIG_PATH_PROPERTY)).orElse(LQLINT_CONFIG);
            Config config = loadConfig(resourceAccessor, configPath);
            if (config != null) {
                return config;
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
