package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.whiteclarkegroup.liquibaselinter.config.rules.generic.PatternRule;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public enum RuleType {

    SCHEMA_NAME("schema-name", PatternRule::new, "Schema name does not follow pattern");

    private String key;
    private Function<RuleConfig, Rule> factory;
    private String defaultErrorMessage;

    RuleType(String key, Function<RuleConfig, Rule> factory, String defaultErrorMessage) {
        this.key = key;
        this.factory = factory;
        this.defaultErrorMessage = defaultErrorMessage;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultErrorMessage() {
        return defaultErrorMessage;
    }

    public Optional<Rule> create(Map<String, RuleConfig> ruleConfigs) {
        if (!ruleConfigs.containsKey(key)) {
            return Optional.empty();
        }
        final RuleConfig ruleConfig = ruleConfigs.get(key);
        if (ruleConfig != null) {
            return Optional.of(this.factory.apply(ruleConfig));
        }
        return Optional.of(this.factory.apply(RuleConfig.enabled()));
    }
}
