package com.whiteclarkegroup.liquibaselinter.config;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class ConfigLoader {

    public static final String LQLLINT_CONFIG = "/lqllint.json";

    public Config load(ResourceAccessor resourceAccessor) {
        try {
            Set<InputStream> resourcesAsStream = resourceAccessor.getResourcesAsStream(LQLLINT_CONFIG);
            if (resourcesAsStream != null && !resourcesAsStream.isEmpty()) {
                try (InputStream inputStream = resourcesAsStream.iterator().next()) {
                    return Config.fromInputStream(inputStream);
                }
            }
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException("Failed to load lq lint config file", e);
        }
        throw new UnexpectedLiquibaseException("Failed to load lq lint config file");
    }

}
