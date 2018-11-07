package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import liquibase.change.AddColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateColumnNullableConstraintRuleImplTest {

    private CreateColumnNullableConstraintRuleImpl createColumnNullableConstraint;

    @BeforeEach
    void setUp() {
        createColumnNullableConstraint = new CreateColumnNullableConstraintRuleImpl();
    }

    @DisplayName("Null constraints should be invalid")
    @Test
    void nullConstraintsShouldBeInvalid() {
        final AddColumnChange addColumnChange = mockAddColumnChangeWithConstraints(null);
        assertTrue(createColumnNullableConstraint.invalid(addColumnChange));
    }

    @DisplayName("Null nullable attribute should be invalid")
    @Test
    void nullNullableAttributeShouldBeInvalid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        final AddColumnChange addColumnChange = mockAddColumnChangeWithConstraints(constraintsConfig);
        assertNull(constraintsConfig.isNullable());
        assertTrue(createColumnNullableConstraint.invalid(addColumnChange));
    }

    @DisplayName("Not null nullable attribute should be valid")
    @Test
    void notNullNullableAttributeShouldBeInvalid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setNullable(Boolean.TRUE);
        final AddColumnChange addColumnChange = mockAddColumnChangeWithConstraints(constraintsConfig);
        assertTrue(constraintsConfig.isNullable());
        assertFalse(createColumnNullableConstraint.invalid(addColumnChange));
    }

    private AddColumnChange mockAddColumnChangeWithConstraints(ConstraintsConfig constraintsConfig) {
        AddColumnChange addColumnChange = mock(AddColumnChange.class);
        final AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setConstraints(constraintsConfig);
        when(addColumnChange.getColumns()).thenReturn(Collections.singletonList(addColumnConfig));
        return addColumnChange;
    }

}
