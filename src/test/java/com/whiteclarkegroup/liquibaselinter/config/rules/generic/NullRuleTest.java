package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

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
    void nullShouldBeValid() {
        assertFalse(nullRule.invalid(null, null));
    }

    @DisplayName("Not null should be invalid")
    @Test
    void notNullShouldBeValid() {
        assertTrue(nullRule.invalid("TEST", null));
    }
}
