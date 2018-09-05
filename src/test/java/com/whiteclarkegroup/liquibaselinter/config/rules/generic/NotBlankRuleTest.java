package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

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
    void emptyStringShouldBeInvalid() {
        assertTrue(notBlankRule.invalid("", null));
    }

    @DisplayName("Null string should be invalid")
    @Test
    void nullStringShouldBeInvalid() {
        assertTrue(notBlankRule.invalid(null, null));
    }

    @DisplayName("Populated string should be valid")
    @Test
    void populatedStringShouldBeValid() {
        assertFalse(notBlankRule.invalid("TEST", null));
    }
}
