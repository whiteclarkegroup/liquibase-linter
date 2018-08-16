package com.wcg.liquibase.config.rules.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotBlankRuleTest {

    private NotBlankRule notBlankRule;

    @BeforeEach
    void setUp() {
        notBlankRule = new NotBlankRule(null);
    }

    @DisplayName("Empty string should be invalid")
    @Test
    void empty_string_should_be_invalid() {
        assertTrue(notBlankRule.invalid("", null));
    }

    @DisplayName("Null string should be invalid")
    @Test
    void null_string_should_be_invalid() {
        assertTrue(notBlankRule.invalid(null, null));
    }

    @DisplayName("Populated string should be valid")
    @Test
    void populated_string_should_be_valid() {
        assertFalse(notBlankRule.invalid("TEST", null));
    }
}
