package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
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
    void validExceedingMaxLengthShouldBeInvalid() {
        assertTrue(maxLengthRule.invalid("too long", null));
    }

    @DisplayName("Short value should be valid")
    @Test
    void shortValueShouldBeValid() {
        assertFalse(maxLengthRule.invalid("short", null));
    }

    @DisplayName("Null value should be valid")
    @Test
    void nullValueShouldBeValid() {
        assertFalse(maxLengthRule.invalid(null, null));
    }

}
