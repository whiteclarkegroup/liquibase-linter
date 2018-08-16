package com.wcg.liquibase.config.rules.generic;

import com.wcg.liquibase.config.RuleConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxLengthRuleTest {

    private MaxLengthRule maxLengthRule;

    @BeforeEach
    void setUp() {
        maxLengthRule = new MaxLengthRule(RuleConfig.builder().withMaxLength(5).build());
    }

    @DisplayName("Value exceeding max length should be invalid")
    @Test
    void valid_exceeding_max_length_should_be_invalid() {
        assertTrue(maxLengthRule.invalid("too long", null));
    }

    @DisplayName("Short value should be valid")
    @Test
    void short_value_should_be_valid() {
        assertFalse(maxLengthRule.invalid("short", null));
    }

    @DisplayName("Null value should be valid")
    @Test
    void null_value_should_be_valid() {
        assertFalse(maxLengthRule.invalid(null, null));
    }

}
