package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

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
    void nullShouldBeInvalid() {
        assertTrue(notNullRule.invalid(null, null));
    }

    @DisplayName("Not null should be valid")
    @Test
    void notNullShouldBeValid() {
        assertFalse(notNullRule.invalid("TEST", null));
    }
}
