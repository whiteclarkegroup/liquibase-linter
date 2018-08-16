package com.wcg.liquibase.config.rules.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NullRuleTest {

    private NullRule nullRule;

    @BeforeEach
    void setUp() {
        nullRule = new NullRule(null);
    }

    @DisplayName("Null should be valid")
    @Test
    void null_should_be_valid() {
        assertFalse(nullRule.invalid(null, null));
    }

    @DisplayName("Not null should be invalid")
    @Test
    void not_null_should_be_valid() {
        assertTrue(nullRule.invalid("TEST", null));
    }
}
