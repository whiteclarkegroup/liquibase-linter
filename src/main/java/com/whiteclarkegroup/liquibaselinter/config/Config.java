package com.whiteclarkegroup.liquibaselinter.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    private final Pattern ignoreContextPattern;
    @JsonDeserialize(using = RuleConfigDeserializer.class)
    private final ListMultimap<String, RuleConfig> rules;
    private final boolean failFast;

    @JsonCreator
    public Config(@JsonProperty("ignore-context-pattern") String ignoreContextPatternString,
                  @JsonProperty("rules") ListMultimap<String, RuleConfig> rules,
                  @JsonProperty("fail-fast") boolean failFast) {
        this.ignoreContextPattern = ignoreContextPatternString != null ? Pattern.compile(ignoreContextPatternString) : null;
        this.rules = rules;
        this.failFast = failFast;
    }

    public static Config fromInputStream(final InputStream inputStream) throws IOException {
        return new ObjectMapper().readValue(inputStream, Config.class);
    }

    public Pattern getIgnoreContextPattern() {
        return ignoreContextPattern;
    }

    public ListMultimap<String, RuleConfig> getRules() {
        return rules;
    }

    public List<RuleConfig> forRule(String ruleName) {
        return rules.get(ruleName);
    }

    public RuleRunner getRuleRunner() {
        return new RuleRunner(this);
    }

    public boolean isRuleEnabled(String name) {
        return rules.containsKey(name) && rules.get(name).stream().anyMatch(RuleConfig::isEnabled);
    }

    public boolean isFailFast() {
        return failFast;
    }

    static class RuleConfigDeserializer extends JsonDeserializer<Object> {

        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        private static final TypeReference<Map<String, Object>> VALUE_TYPE_REF = new TypeReference<Map<String, Object>>() {
        };

        @Override
        public ListMultimap<String, RuleConfig> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
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

}
