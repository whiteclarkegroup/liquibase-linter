package com.whiteclarkegroup.liquibaselinter.config.rules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RuleTypeTest {

    @DisplayName("Should not return rule when key not in map")
    @Test
    void shouldNotReturnRuleWhenKeyNotInMap() {
        Optional<Rule> rule = RuleType.SCHEMA_NAME.create(new HashMap<>());
        assertFalse(rule.isPresent());
    }

    @DisplayName("Should return rule when key")
    @Test
    void shouldReturnRuleWhenKeyInMap() {
        Map<String, RuleConfig> ruleConfigs = new HashMap<>();
        ruleConfigs.put(RuleType.SCHEMA_NAME.getKey(), RuleConfig.builder().build());
        Optional<Rule> rule = RuleType.SCHEMA_NAME.create(ruleConfigs);
        assertTrue(rule.isPresent());
        assertNotNull(rule.get());
    }

    @DisplayName("Should return rule when key in map even is value is null")
    @Test
    void shouldReturnRuleWhenKeyInMapEvenIfValueIsNull() {
        Map<String, RuleConfig> ruleConfigs = new HashMap<>();
        ruleConfigs.put(RuleType.SCHEMA_NAME.getKey(), null);
        Optional<Rule> rule = RuleType.SCHEMA_NAME.create(ruleConfigs);
        assertTrue(rule.isPresent());
        assertNotNull(rule.get());
    }

}
