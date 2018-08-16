package com.wcg.liquibase.config.rules.specific;

import liquibase.change.ConstraintsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateColumnNullableContraintTest {

    private CreateColumnNullableConstraint createColumnNullableConstraint;

    @BeforeEach
    void setUp() {
        createColumnNullableConstraint = new CreateColumnNullableConstraint(null);
    }

    @DisplayName("Null constraints should be invalid")
    @Test
    void null_constraints_should_be_invalid() {
        assertTrue(createColumnNullableConstraint.invalid(null, null));
    }

    @DisplayName("Null nullable attribute should be invalid")
    @Test
    void null_nullable_attribute_should_be_invalid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        assertNull(constraintsConfig.isNullable());
        assertTrue(createColumnNullableConstraint.invalid(constraintsConfig, null));
    }

    @DisplayName("Not null nullable attribute should be valid")
    @Test
    void not_null_nullable_attribute_should_be_invalid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setNullable(Boolean.TRUE);
        assertTrue(constraintsConfig.isNullable());
        assertFalse(createColumnNullableConstraint.invalid(constraintsConfig, null));
    }

}
