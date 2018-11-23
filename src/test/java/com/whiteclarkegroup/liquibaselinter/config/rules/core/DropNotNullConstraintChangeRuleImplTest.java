package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.DropNotNullConstraintChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({ChangeSetParameterResolver.class})
class DropNotNullConstraintChangeRuleImplTest {

    private DropNotNullRequireColumnDataTypeRuleImpl dropNotNullRequireColumnDataTypeRule;

    @BeforeEach
    void setUp() {
        dropNotNullRequireColumnDataTypeRule = new DropNotNullRequireColumnDataTypeRuleImpl();
    }

    @DisplayName("Should allow non null column data type")
    @Test
    void shouldAllowNonNullColumnDataType(ChangeSet changeSet) {
        assertFalse(dropNotNullRequireColumnDataTypeRule.invalid(build(changeSet, "NVARCHAR(10)")));
    }

    @DisplayName("Should not allow null column data type")
    @Test
    void shouldNotAllowNullColumnDataType(ChangeSet changeSet) {
        assertTrue(dropNotNullRequireColumnDataTypeRule.invalid(build(changeSet, null)));
    }

    @DisplayName("Should not allow blank column data type")
    @Test
    void shouldNotAllowBlankColumnDataType(ChangeSet changeSet) {
        assertTrue(dropNotNullRequireColumnDataTypeRule.invalid(build(changeSet, "")));
    }

    private DropNotNullConstraintChange build(ChangeSet changeSet, String columnDataType) {
        DropNotNullConstraintChange change = new DropNotNullConstraintChange();
        change.setColumnDataType(columnDataType);
        change.setChangeSet(changeSet);
        return change;
    }

}
