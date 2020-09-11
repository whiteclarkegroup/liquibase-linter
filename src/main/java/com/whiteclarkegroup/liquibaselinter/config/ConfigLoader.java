package com.whiteclarkegroup.liquibaselinter.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;
import liquibase.util.ObjectUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.System.getProperty;
import static java.util.stream.Collectors.toList;

public class ConfigLoader {

    public static final String LQLINT_CONFIG = "/lqlint.json";
    public static final String LQLINT_CONFIG_CLASSPATH = "lqlint.json";
    public static final String LQLINT_CONFIG_PATH_PROPERTY = "lqlint.config.path";

    public Config load(ResourceAccessor resourceAccessor) {
        List<String> configPaths = Stream.of(getProperty(LQLINT_CONFIG_PATH_PROPERTY), LQLINT_CONFIG, LQLINT_CONFIG_CLASSPATH).filter(Objects::nonNull).collect(toList());
        try {
            for (String configPath : configPaths) {
                Config config = loadConfig(resourceAccessor, configPath);
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
                return loadImports(resourceAccessor, Config.fromInputStream(inputStream));
            }
        }
        return null;
    }

    private Config loadImports(ResourceAccessor resourceAccessor, Config config) {
        if (config.getImports() == null || config.getImports().isEmpty()) {
            return config;
        }
        final ListMultimap<String, RuleConfig> configRules = ObjectUtil.defaultIfNull(config.getRules(), ArrayListMultimap.create());
        final ImmutableListMultimap.Builder<String, RuleConfig> combinedRules = new ImmutableListMultimap.Builder<>();
        combinedRules.putAll(configRules);

        config.getImports().forEach(path -> {
            try {
                final Config importedConfig = loadConfig(resourceAccessor, path);
                if (importedConfig == null) {
                    throw new UnexpectedLiquibaseException("Failed to load imported lq lint config file: " + path);
                }
                final ListMultimap<String, RuleConfig> importedRules = importedConfig.getRules();
                if (importedRules != null) {
                    importedRules.asMap().forEach((key, rules) -> {
                        if (!configRules.containsKey(key)) {
                            combinedRules.putAll(key, rules);
                        }
                    });
                }
            } catch (IOException e) {
                throw new UnexpectedLiquibaseException("Failed to load imported lq lint config file: " + path, e);
            }
        });
        return new Config.Builder(config).withRules(combinedRules.build()).withImports().build();
    }

}
