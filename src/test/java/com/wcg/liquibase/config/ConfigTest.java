package com.wcg.liquibase.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {

    @Test
    void should_not_fail_on_null_override_mixin() {
        Config config = mockConfig(buildSchemaNameRule(false));
        config.mixin(null);
        assertFalse(config.getRules().getSchemaName().isEnabled());
    }

    @Test
    void should_mixin_overrides() {
        Config config = mockConfig(buildSchemaNameRule(false));
        assertFalse(config.getRules().getSchemaName().isEnabled());
        Config override = mockConfig(buildSchemaNameRule(true));
        config = config.mixin(override);
        assertTrue(config.getRules().getSchemaName().isEnabled());
    }

    @Test
    void should_override_rule_if_null() {
        Config config = mockConfig(buildSchemaNameRule(false));
        assertFalse(config.getRules().getSchemaName().isEnabled());
        Config override = mockConfig(buildSchemaNameRule(false));
        config = config.mixin(override);
        assertFalse(config.getRules().getSchemaName().isEnabled());
    }

    @Test
    void should_not_force_to_override_error_message() {
        Config config = mockConfig(buildRuleWithErrorMessage("Default Error Message"));
        assertEquals(config.getRules().getSchemaName().getErrorMessage(), "Default Error Message");
        Config override = mockConfig(buildRuleWithErrorMessage(null));
        config = config.mixin(override);
        assertEquals(config.getRules().getSchemaName().getErrorMessage(), "Default Error Message");
    }

    @Test
    void should_be_able_to_override_error_message() {
        Config config = mockConfig(buildRuleWithErrorMessage("Default Error Message"));
        assertEquals(config.getRules().getSchemaName().getErrorMessage(), "Default Error Message");
        Config override = mockConfig(buildRuleWithErrorMessage("Override Error Message"));
        config = config.mixin(override);
        assertEquals(config.getRules().getSchemaName().getErrorMessage(), "Override Error Message");
    }

    private RuleConfig buildSchemaNameRule(boolean enabled) {
        return RuleConfig.builder().withEnabled(enabled).build();
    }

    private RuleConfig buildRuleWithErrorMessage(String errorMessage) {
        return RuleConfig.builder().withEnabled(true).withErrorMessage(errorMessage).build();
    }

    private Config mockConfig(RuleConfig schemaNameRule) {
        Rules rules = new Rules(null, schemaNameRule, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        return new Config(null, rules);
    }
}
