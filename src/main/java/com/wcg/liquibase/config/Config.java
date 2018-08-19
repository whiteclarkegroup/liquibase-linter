package com.wcg.liquibase.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcg.liquibase.config.rules.RuleRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    private final Pattern ignoreContextPattern;
    private final String ignoreContextPatternString;
    private final Map<String, RuleConfig> rules;

    @JsonCreator
    public Config(@JsonProperty("ignore-context-pattern") String ignoreContextPatternString, @JsonProperty("rules") Map<String, RuleConfig> rules) {
        this.ignoreContextPatternString = ignoreContextPatternString;
        this.ignoreContextPattern = ignoreContextPatternString != null ? Pattern.compile(ignoreContextPatternString) : null;
        this.rules = rules;
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

    public RuleRunner getRuleRunner() {
        return new RuleRunner(this.rules);
    }

    Config mixin(Config toMix) {
        if (toMix == null) {
            return this;
        }
        Map<String, RuleConfig> mixedRules =
                rules.entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().mixin(toMix.getRules().get(e.getKey()))));
        return new Config(
                ofNullable(toMix.ignoreContextPatternString).orElse(ignoreContextPatternString),
                mixedRules
        );
    }
}
