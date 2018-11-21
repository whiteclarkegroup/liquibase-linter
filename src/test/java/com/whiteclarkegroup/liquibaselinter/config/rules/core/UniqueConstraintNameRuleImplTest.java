package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.core.AddUniqueConstraintChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniqueConstraintNameRuleImplTest {

    private UniqueConstraintNameRuleImpl uniqueConstraintNameRule;

    @BeforeEach
    void setUp() {
        uniqueConstraintNameRule = new UniqueConstraintNameRuleImpl();
    }

    @DisplayName("Unique constraint name must not be null")
    @Test
    void uniqueConstraintNameNameMustNotBeNull() {
        assertTrue(uniqueConstraintNameRule.invalid(getAddUniqueConstraintChange(null)));
    }

    @DisplayName("Unique constraint name must follow pattern")
    @Test
    void uniqueConstraintNameNameMustFollowPattern() {
        uniqueConstraintNameRule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build());

        assertTrue(uniqueConstraintNameRule.invalid(getAddUniqueConstraintChange("TBL_INVALID")));

        assertFalse(uniqueConstraintNameRule.invalid(getAddUniqueConstraintChange("TABLE_VALID")));
    }

    @DisplayName("Unique constraint name rule should support formatted error message with pattern arg")
    @Test
    void uniqueConstraintNameNameRuleShouldReturnFormattedErrorMessage() {
        uniqueConstraintNameRule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").withErrorMessage("Unique constraint name '%s' must follow pattern '%s'").build());
        assertEquals(uniqueConstraintNameRule.getMessage(getAddUniqueConstraintChange("TBL_INVALID")), "Unique constraint name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
    }

    private AddUniqueConstraintChange getAddUniqueConstraintChange(String constraintName) {
        AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
        addUniqueConstraintChange.setConstraintName(constraintName);
        return addUniqueConstraintChange;
    }

}
