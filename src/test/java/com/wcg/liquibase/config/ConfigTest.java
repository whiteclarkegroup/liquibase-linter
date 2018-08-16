package com.wcg.liquibase.config;

import com.google.common.collect.ImmutableMap;
import com.wcg.liquibase.config.rules.RuleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {

    @Test
    void should_not_fail_on_null_override_mixin() {
        Config config = mockConfig(buildSchemaNameRule(false));
        config.mixin(null);
        assertFalse(RuleType.SCHEMA_NAME.create(config.getRules()).isEnabled());
    }

    @Test
    void should_mixin_overrides() {
        Config config = mockConfig(buildSchemaNameRule(false));
        assertFalse(RuleType.SCHEMA_NAME.create(config.getRules()).isEnabled());
        Config override = mockConfig(buildSchemaNameRule(true));
        config = config.mixin(override);
        assertTrue(RuleType.SCHEMA_NAME.create(config.getRules()).isEnabled());
    }

    @Test
    void should_override_rule_if_null() {
        Config config = mockConfig(buildSchemaNameRule(false));
        assertFalse(RuleType.SCHEMA_NAME.create(config.getRules()).isEnabled());
        Config override = mockConfig(buildSchemaNameRule(false));
        config = config.mixin(override);
        assertFalse(RuleType.SCHEMA_NAME.create(config.getRules()).isEnabled());
    }

    @Test
    void should_not_force_to_override_error_message() {
        Config config = mockConfig(buildRuleWithErrorMessage("Default Error Message"));
        assertEquals(RuleType.SCHEMA_NAME.create(config.getRules()).getErrorMessage(), "Default Error Message");
        Config override = mockConfig(buildRuleWithErrorMessage(null));
        config = config.mixin(override);
        assertEquals(RuleType.SCHEMA_NAME.create(config.getRules()).getErrorMessage(), "Default Error Message");
    }

    @Test
    void should_be_able_to_override_error_message() {
        Config config = mockConfig(buildRuleWithErrorMessage("Default Error Message"));
        assertEquals(RuleType.SCHEMA_NAME.create(config.getRules()).getErrorMessage(), "Default Error Message");
        Config override = mockConfig(buildRuleWithErrorMessage("Override Error Message"));
        config = config.mixin(override);
        assertEquals(RuleType.SCHEMA_NAME.create(config.getRules()).getErrorMessage(), "Override Error Message");
    }

    private RuleConfig buildSchemaNameRule(boolean enabled) {
        return RuleConfig.builder().withEnabled(enabled).build();
    }

    private RuleConfig buildRuleWithErrorMessage(String errorMessage) {
        return RuleConfig.builder().withEnabled(true).withErrorMessage(errorMessage).build();
    }

    private Config mockConfig(RuleConfig schemaNameRule) {
        return new Config(null, ImmutableMap.of(RuleType.SCHEMA_NAME.getKey(), schemaNameRule));
    }
}
