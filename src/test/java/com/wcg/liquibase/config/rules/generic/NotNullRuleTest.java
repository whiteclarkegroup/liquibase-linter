package com.wcg.liquibase.config.rules.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotNullRuleTest {

    private NotNullRule notNullRule;

    @BeforeEach
    void setUp() {
        notNullRule = new NotNullRule(null);
    }

    @DisplayName("Null should be invalid")
    @Test
    void null_should_be_invalid() {
        assertTrue(notNullRule.invalid(null, null));
    }

    @DisplayName("Not null should be valid")
    @Test
    void not_null_should_be_valid() {
        assertFalse(notNullRule.invalid("TEST", null));
    }
}
