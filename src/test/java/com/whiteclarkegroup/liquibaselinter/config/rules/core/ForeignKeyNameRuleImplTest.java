package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.core.AddForeignKeyConstraintChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForeignKeyNameRuleImplTest {

    private ForeignKeyNameRuleImpl foreignKeyNameRule;

    @BeforeEach
    void setUp() {
        foreignKeyNameRule = new ForeignKeyNameRuleImpl();
    }

    @DisplayName("Foreign key name must not be null")
    @Test
    void foreignKeyNameMustNotBeNull() {
        assertTrue(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange(null)));
    }

    @DisplayName("Foreign key name must follow pattern basic")
    @Test
    void foreignKeyNameMustFollowPatternBasic() {
        foreignKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_FK$").build());
        assertTrue(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange("INVALID_FK")));
        assertFalse(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange("VALID_FK")));
    }

    @DisplayName("Foreign key name must follow pattern dynamic value")
    @Test
    void foreignKeyNameMustFollowPatternDynamicValue() {
        foreignKeyNameRule.configure(RuleConfig.builder().withPattern("^{{value}}_FK$").withDynamicValue("baseTableName + '_' + referencedTableName").build());
        assertTrue(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange("INVALID_FK")));
        assertFalse(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange("BASE_REFERENCED_FK")));
    }

    @DisplayName("Foreign key name rule should support formatted error message with pattern arg")
    @Test
    void foreignKeyNameRuleShouldReturnFormattedErrorMessage() {
        foreignKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_FK$").withErrorMessage("Foreign key constraint '%s' must follow pattern '%s'").build());
        assertEquals(foreignKeyNameRule.getMessage(getAddForeignKeyConstraintChange("INVALID_FK")), "Foreign key constraint 'INVALID_FK' must follow pattern '^VALID_FK$'");
    }

    private AddForeignKeyConstraintChange getAddForeignKeyConstraintChange(String constraintName) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setConstraintName(constraintName);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCED");
        return addForeignKeyConstraintChange;
    }
}
