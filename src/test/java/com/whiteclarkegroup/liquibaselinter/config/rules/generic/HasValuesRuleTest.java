package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HasValuesRuleTest {

    private HasValuesRule hasValuesRule;

    @BeforeEach
    void setUp() {
        hasValuesRule = new HasValuesRule(null);
    }

    @DisplayName("Empty list should be invalid")
    @Test
    void emptyListShouldBeInvalid() {
        assertTrue(hasValuesRule.invalid(new ArrayList<>(), null));
    }

    @DisplayName("Null list should be invalid")
    @Test
    void nullListShouldBeInvalid() {
        assertTrue(hasValuesRule.invalid(null, null));
    }

    @DisplayName("Populated list should be valid")
    @Test
    void populatedListShouldBeValid() {
        assertFalse(hasValuesRule.invalid(Collections.singleton("TEST"), null));
    }
}
