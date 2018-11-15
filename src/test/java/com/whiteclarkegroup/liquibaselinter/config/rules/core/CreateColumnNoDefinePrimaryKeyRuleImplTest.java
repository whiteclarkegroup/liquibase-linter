package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import liquibase.change.AddColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateColumnNoDefinePrimaryKeyRuleImplTest {

    private CreateColumnNoDefinePrimaryKeyRuleImpl createColumnNoDefinePrimaryKeyRule;

    @BeforeEach
    void setUp() {
        createColumnNoDefinePrimaryKeyRule = new CreateColumnNoDefinePrimaryKeyRuleImpl();
    }

    @DisplayName("Null primary key attribute should be valid")
    @Test
    void nullPrimaryKeyAttributeShouldBeValid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        assertNull(constraintsConfig.isPrimaryKey());
        assertFalse(createColumnNoDefinePrimaryKeyRule.invalid(buildAddColumnChange(null)));
    }

    @DisplayName("False primary key attribute should be valid")
    @Test
    void falsePrimaryKeyAttributeShouldBeValid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setPrimaryKey(Boolean.FALSE);
        assertFalse(constraintsConfig.isPrimaryKey());
        assertFalse(createColumnNoDefinePrimaryKeyRule.invalid(buildAddColumnChange(Boolean.FALSE)));
    }

    @DisplayName("False primary key attribute should be valid")
    @Test
    void falsePrimaryKeyAttributeShouldBeInvalid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setPrimaryKey(Boolean.TRUE);
        assertTrue(constraintsConfig.isPrimaryKey());
        assertTrue(createColumnNoDefinePrimaryKeyRule.invalid(buildAddColumnChange(Boolean.TRUE)));
    }

    private AddColumnChange buildAddColumnChange(Boolean primaryKey) {
        AddColumnChange addColumnChange = new AddColumnChange();
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        ConstraintsConfig constraints = new ConstraintsConfig();
        constraints.setPrimaryKey(primaryKey);
        addColumnConfig.setConstraints(constraints);
        addColumnChange.getColumns().add(addColumnConfig);
        return addColumnChange;
    }

}
