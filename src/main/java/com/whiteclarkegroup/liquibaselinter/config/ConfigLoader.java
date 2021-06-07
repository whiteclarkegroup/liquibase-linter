package com.whiteclarkegroup.liquibaselinter.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.report.Reporter;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.System.getProperty;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ConfigLoader {

    public static final String LQLINT_CONFIG = "/lqlint.json";
    public static final String LQLINT_CONFIG_CLASSPATH = "lqlint.json";
    public static final String LQLINT_CONFIG_PATH_PROPERTY = "lqlint.config.path";
    public static final String LQLINT_CONFIG_IMPLICIT_PATH = "com/whiteclarkegroup/liquibaselinter/config/lqlint-implicit.json";

    public Config load(ResourceAccessor resourceAccessor) {
        List<String> configPaths = Stream.of(getProperty(LQLINT_CONFIG_PATH_PROPERTY), LQLINT_CONFIG, LQLINT_CONFIG_CLASSPATH).filter(Objects::nonNull).collect(toList());
        try {
            for (String configPath : configPaths) {
                final Config config = loadConfig(resourceAccessor, configPath);
                if (config != null) {
                    return loadImports(resourceAccessor, config, singletonList(LQLINT_CONFIG_IMPLICIT_PATH));
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
                final Config config = Config.fromInputStream(inputStream);
                return loadImports(resourceAccessor, config, config.getImports());
            }
        }
        return null;
    }

    private Config loadImports(ResourceAccessor resourceAccessor, Config config, List<String> imports) {
        final ListMultimap<String, RuleConfig> rules = ArrayListMultimap.create();
        final ListMultimap<String, Reporter> reporting = ArrayListMultimap.create();

        if (config.getRules() != null) {
            rules.putAll(config.getRules());
        }
        if (config.getReporting() != null) {
            reporting.putAll(config.getReporting());
        }
        if (imports != null) {
            for (String path : imports) {
                try {
                    final Config importedConfig = loadConfig(resourceAccessor, path);
                    combine(config.getRules(), importedConfig.getRules(), rules);
                    combine(config.getReporting(), importedConfig.getReporting(), reporting);
                } catch (IOException | NullPointerException e) {
                    throw new UnexpectedLiquibaseException("Failed to load imported lq lint config file: " + path, e);
                }
            }
        }
        return new Config.Builder(config).withRules(rules).withReporting(reporting).withImports().build();
    }

    private static <T> void combine(ListMultimap<String, T> config,
                                    ListMultimap<String, T> imported,
                                    ListMultimap<String, T> combined) {
        if (imported != null) {
            imported.asMap().forEach((key, importedRulesList) -> {
                // If the main config has a config of the same name, it overrides any imported config for the same
                // name. But if not config of the same name exists in the main config, merge all the imported
                // configs together. This could cause multiple configs of the same name from different imported
                // files to end up in the final merged config.
                if (config == null || !config.containsKey(key)) {
                    combined.putAll(key, importedRulesList);
                }
            });
        }
    }
}
