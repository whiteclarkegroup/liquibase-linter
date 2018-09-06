package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import liquibase.change.ConstraintsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateColumnNoDefinePrimaryKeyTest {

    private CreateColumnNoDefinePrimaryKey createColumnNoDefinePrimaryKey;

    @BeforeEach
    void setUp() {
        createColumnNoDefinePrimaryKey = new CreateColumnNoDefinePrimaryKey(null);
    }

    @DisplayName("Null constraints should be invalid")
    @Test
    void nullConstraintsShouldBeInvalid() {
        assertTrue(createColumnNoDefinePrimaryKey.invalid(null, null));
    }

    @DisplayName("Null primary key attribute should be valid")
    @Test
    void nullPrimaryKeyAttributeShouldBeValid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        assertNull(constraintsConfig.isPrimaryKey());
        assertFalse(createColumnNoDefinePrimaryKey.invalid(constraintsConfig, null));
    }

    @DisplayName("False primary key attribute should be valid")
    @Test
    void falsePrimaryKeyAttributeShouldBeValid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setPrimaryKey(Boolean.FALSE);
        assertFalse(constraintsConfig.isPrimaryKey());
        assertFalse(createColumnNoDefinePrimaryKey.invalid(constraintsConfig, null));
    }

    @DisplayName("False primary key attribute should be valid")
    @Test
    void falsePrimaryKeyAttributeShouldBeInvalid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setPrimaryKey(Boolean.TRUE);
        assertTrue(constraintsConfig.isPrimaryKey());
        assertTrue(createColumnNoDefinePrimaryKey.invalid(constraintsConfig, null));
    }

}
