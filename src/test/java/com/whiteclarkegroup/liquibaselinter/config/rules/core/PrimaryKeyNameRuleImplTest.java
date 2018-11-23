package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.core.AddPrimaryKeyChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimaryKeyNameRuleImplTest {

    private PrimaryKeyNameRuleImpl primaryKeyNameRule;

    @BeforeEach
    void setUp() {
        primaryKeyNameRule = new PrimaryKeyNameRuleImpl();
    }

    @DisplayName("Primary key name must not be null")
    @Test
    void primaryKeyNameMustNotBeNull() {
        assertTrue(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange(null)));
    }

    @DisplayName("Primary key name must follow pattern basic")
    @Test
    void primaryKeyNameMustFollowPatternBasic() {
        primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());
        assertTrue(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("INVALID_PK")));
        assertFalse(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("VALID_PK")));
    }

    @DisplayName("Primary key name must follow pattern dynamic value")
    @Test
    void primaryKeyNameMustFollowPatternDynamicValue() {
        primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^{{value}}_PK$").withDynamicValue("tableName").build());
        assertTrue(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("INVALID_PK")));
        assertFalse(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("TABLE_PK")));
    }

    @DisplayName("Primary key name rule should support formatted error message with pattern arg")
    @Test
    void primaryKeyNameRuleShouldReturnFormattedErrorMessage() {
        primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").withErrorMessage("Primary key constraint '%s' must follow pattern '%s'").build());
        assertEquals(primaryKeyNameRule.getMessage(getAddPrimaryKeyConstraintChange("INVALID_PK")), "Primary key constraint 'INVALID_PK' must follow pattern '^VALID_PK$'");
    }

    private AddPrimaryKeyChange getAddPrimaryKeyConstraintChange(String constraintName) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setConstraintName(constraintName);
        addPrimaryKeyChange.setTableName("TABLE");
        return addPrimaryKeyChange;
    }
}
