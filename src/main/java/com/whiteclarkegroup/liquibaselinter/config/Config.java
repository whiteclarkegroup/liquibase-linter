package com.whiteclarkegroup.liquibaselinter.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Streams;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.report.Reporter;
import com.whiteclarkegroup.liquibaselinter.report.ReporterConfig;
import liquibase.exception.UnexpectedLiquibaseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@JsonDeserialize(builder = Config.Builder.class)
public class Config {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> VALUE_TYPE_REF = new TypeReference<Map<String, Object>>() {
    };

    private final Pattern ignoreContextPattern;
    private final Pattern ignoreFilesPattern;
    private final ListMultimap<String, RuleConfig> rules;
    private final boolean failFast;
    private final String enableAfter;
    private final ListMultimap<String, Reporter> reporting;
    private final List<String> imports;

    private Config(Pattern ignoreContextPattern,
                   Pattern ignoreFilesPattern,
                   ListMultimap<String, RuleConfig> rules,
                   boolean failFast,
                   String enableAfter,
                   ListMultimap<String, Reporter> reporting,
                   List<String> imports) {
        this.ignoreContextPattern = ignoreContextPattern;
        this.ignoreFilesPattern = ignoreFilesPattern;
        this.rules = Optional.ofNullable(rules).map(ImmutableListMultimap::copyOf).orElse(ImmutableListMultimap.of());
        this.failFast = failFast;
        this.enableAfter = enableAfter;
        this.reporting = Optional.ofNullable(reporting).map(ImmutableListMultimap::copyOf).orElse(ImmutableListMultimap.of());
        this.imports = Optional.ofNullable(imports).map(ImmutableList::copyOf).orElse(ImmutableList.of());
    }

    public static Config fromInputStream(final InputStream inputStream) throws IOException {
        return new ObjectMapper().readValue(inputStream, Config.class);
    }

    public Pattern getIgnoreContextPattern() {
        return ignoreContextPattern;
    }

    public Pattern getIgnoreFilesPattern() {
        return ignoreFilesPattern;
    }

    public ListMultimap<String, RuleConfig> getRules() {
        return rules;
    }

    public List<RuleConfig> forRule(String ruleName) {
        return rules.get(ruleName);
    }

    public List<RuleConfig> getEnabledRuleConfig(String ruleName) {
        return forRule(ruleName).stream().filter(RuleConfig::isEnabled).collect(Collectors.toList());
    }

    public boolean isFailFast() {
        return failFast;
    }

    public String getEnableAfter() {
        return enableAfter;
    }

    public ListMultimap<String, Reporter> getReporting() {
        return this.reporting;
    }

    List<String> getImports() {
        return this.imports;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        private Pattern ignoreContextPattern;
        private Pattern ignoreFilesPattern;
        private ListMultimap<String, RuleConfig> rules = ImmutableListMultimap.of();
        private boolean failFast;
        private String enableAfter;
        private ListMultimap<String, Reporter> reporting = ImmutableListMultimap.of();
        private List<String> imports = Collections.emptyList();

        public Builder() {
            // Used primarily by JSON deserialization
        }

        public Builder(Config config) {
            this.ignoreContextPattern = config.getIgnoreContextPattern();
            this.ignoreFilesPattern = config.getIgnoreFilesPattern();
            this.rules = config.getRules();
            this.failFast = config.isFailFast();
            this.enableAfter = config.getEnableAfter();
            this.imports = config.getImports();
        }

        @JsonProperty("ignore-context-pattern")
        public Builder withIgnoreContextPattern(String ignoreContextPattern) {
            this.ignoreContextPattern = ignoreContextPattern != null ? Pattern.compile(ignoreContextPattern) : null;
            return this;
        }

        public Builder withIgnoreContextPattern(Pattern ignoreContextPattern) {
            this.ignoreContextPattern = ignoreFilesPattern;
            return this;
        }

        @JsonProperty("ignore-files-pattern")
        public Builder withIgnoreFilesPattern(String ignoreFilesPattern) {
            this.ignoreFilesPattern = ignoreFilesPattern != null ? Pattern.compile(ignoreFilesPattern) : null;
            return this;
        }

        public Builder withIgnoreFilesPattern(Pattern ignoreFilesPattern) {
            this.ignoreFilesPattern = ignoreFilesPattern;
            return this;
        }

        @JsonProperty("rules")
        @JsonDeserialize(using = RuleConfigDeserializer.class)
        public Builder withRules(ListMultimap<String, RuleConfig> rules) {
            this.rules = ImmutableListMultimap.copyOf(rules);
            return this;
        }

        @JsonProperty("fail-fast")
        public Builder withFailFast(boolean failFast) {
            this.failFast = failFast;
            return this;
        }

        @JsonProperty("enable-after")
        public Builder withEnableAfter(String enableAfter) {
            this.enableAfter = enableAfter;
            return this;
        }

        @JsonProperty("reporting")
        @JsonDeserialize(using = ReportingDeserializer.class)
        public Builder withReporting(ListMultimap<String, Reporter> reporting) {
            this.reporting = reporting;
            return this;
        }

        @JsonProperty("import")
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        public Builder withImports(String... imports) {
            this.imports = Collections.unmodifiableList(Arrays.asList(imports));
            return this;
        }

        public Config build() {
            return new Config(ignoreContextPattern, ignoreFilesPattern, rules, failFast, enableAfter, reporting, imports);
        }
    }

    static class RuleConfigDeserializer extends JsonDeserializer<Object> {

        @Override
        public ListMultimap<String, RuleConfig> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
            final Map<String, Object> config = jsonParser.readValueAs(VALUE_TYPE_REF);
            final ImmutableListMultimap.Builder<String, RuleConfig> ruleConfigs = new ImmutableListMultimap.Builder<>();
            config.forEach((key, value) -> {
                if (value instanceof List) {
                    ((List) value).forEach(item -> populateConfigValue(ruleConfigs, key, item));
                } else {
                    populateConfigValue(ruleConfigs, key, value);
                }
            });
            return ruleConfigs.build();
        }

        private void populateConfigValue(ImmutableListMultimap.Builder<String, RuleConfig> ruleConfigs, String key, Object value) {
            try {
                boolean ruleEnabled = OBJECT_MAPPER.convertValue(value, boolean.class);
                ruleConfigs.put(key, ruleEnabled ? RuleConfig.enabled() : RuleConfig.disabled());
            } catch (IllegalArgumentException e) {
                RuleConfig ruleConfig = OBJECT_MAPPER.convertValue(value, RuleConfig.class);
                ruleConfigs.put(key, ruleConfig);
            }
        }
    }

    static class ReportingDeserializer extends JsonDeserializer<Object> {
        private static final ServiceLoader<Reporter.Factory> REPORTERS = ServiceLoader.load(Reporter.Factory.class);

        @Override
        public ListMultimap<String, Reporter> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
            final ImmutableListMultimap.Builder<String, Reporter> reporting = new ImmutableListMultimap.Builder<>();
            final JsonNode config = jsonParser.readValueAsTree();
            config.fields().forEachRemaining(entry -> {
                final String key = entry.getKey();
                final JsonNode value = entry.getValue();
                if (value.isArray()) {
                    value.elements().forEachRemaining(node -> populateConfigValue(reporting, key, node));
                } else {
                    populateConfigValue(reporting, key, value);
                }
            });
            return reporting.build();
        }

        private void populateConfigValue(ImmutableListMultimap.Builder<String, Reporter> reporting, String key, JsonNode node) {
            Reporter.Factory<Reporter, ReporterConfig> reporterFactory = Streams.stream(REPORTERS.iterator())
                .filter(factory -> factory.supports(key))
                .findFirst()
                .orElseThrow(() -> new UnexpectedLiquibaseException("No lq lint reporter named '" + key + '\''));

            if (node.isBoolean()) {
                reporting.put(key, reporterFactory.create(node.asBoolean()));
            } else if (node.isTextual()) {
                reporting.put(key, reporterFactory.create(node.asText()));
            } else {
                reporting.put(key, reporterFactory.create(OBJECT_MAPPER.convertValue(node, reporterFactory.getConfigClass())));
            }
        }
    }

}
