package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.whiteclarkegroup.liquibaselinter.config.rules.generic.GenericRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.generic.MandatoryPatternRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.generic.MaxLengthRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.generic.PatternRule;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public enum RuleType {

    NO_DUPLICATE_INCLUDES("no-duplicate-includes", GenericRule::new, "Changelog file '%s' was included more than once"),
    SCHEMA_NAME("schema-name", PatternRule::new, "Schema name does not follow pattern"),
    OBJECT_NAME("object-name", PatternRule::new, "Object name does not follow pattern"),
    OBJECT_NAME_LENGTH("object-name-length", MaxLengthRule::new, "Object name '%s' must be less than %d characters"),
    UNIQUE_CONSTRAINT_NAME("unique-constraint-name", MandatoryPatternRule::new, "Unique constraint name does not follow pattern"),
    FOREIGN_KEY_NAME("foreign-key-name", MandatoryPatternRule::new, "Foreign key name is missing or does not follow pattern");

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
