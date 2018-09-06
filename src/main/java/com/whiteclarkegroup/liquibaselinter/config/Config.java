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
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    private final Pattern ignoreContextPattern;
    @JsonDeserialize(using = RuleConfigDeserializer.class)
    private final Map<String, RuleConfig> rules;
    private final List<Class> illegalChangeTypes;

    @JsonCreator
    public Config(@JsonProperty("ignore-context-pattern") String ignoreContextPatternString,
                  @JsonProperty("rules") Map<String, RuleConfig> rules,
                  @JsonProperty("illegal-change-types") List<Class> illegalChangeTypes) {
        this.ignoreContextPattern = ignoreContextPatternString != null ? Pattern.compile(ignoreContextPatternString) : null;
        this.rules = rules;
        this.illegalChangeTypes = illegalChangeTypes;
    }

    public static Config fromInputStream(final InputStream inputStream) throws IOException {
        return new ObjectMapper().readValue(inputStream, Config.class);
    }

    public Pattern getIgnoreContextPattern() {
        return ignoreContextPattern;
    }

    public Map<String, RuleConfig> getRules() {
        return rules;
    }

    public List<Class> getIllegalChangeTypes() {
        return illegalChangeTypes;
    }

    public RuleRunner getRuleRunner() {
        return new RuleRunner(this.rules);
    }

    static class RuleConfigDeserializer extends JsonDeserializer<Object> {

        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        private static final TypeReference<Map<String, Object>> VALUE_TYPE_REF = new TypeReference<Map<String, Object>>() {
        };

        @Override
        public Map<String, RuleConfig> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            final Map<String, Object> config = jsonParser.readValueAs(VALUE_TYPE_REF);
            final Map<String, RuleConfig> ruleConfigs = new HashMap<>();
            config.forEach((key, value) -> {
                try {
                    boolean ruleEnabled = OBJECT_MAPPER.convertValue(value, boolean.class);
                    ruleConfigs.put(key, ruleEnabled ? RuleConfig.enabled() : RuleConfig.disabled());
                } catch (IllegalArgumentException e) {
                    RuleConfig ruleConfig = OBJECT_MAPPER.convertValue(value, RuleConfig.class);
                    ruleConfigs.put(key, ruleConfig);
                }
            });
            return ruleConfigs;
        }
    }

}
