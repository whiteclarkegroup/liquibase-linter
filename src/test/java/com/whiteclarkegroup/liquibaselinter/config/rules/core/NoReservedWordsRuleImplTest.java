package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.core.ObjectNameRulesImpl.NoReservedWordsRuleImpl;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoReservedWordsRuleImplTest {

    private NoReservedWordsRuleImpl ruleImpl;

    @BeforeEach
    void setUp() {
        ruleImpl = new NoReservedWordsRuleImpl();
    }


    @DisplayName("Should reject usage of a reserved word")
    @Test
    void objectNameRuleShouldReturnFormattedErrorMessage() {
        ruleImpl.configure(RuleConfig.enabled());

        assertTrue(ruleImpl.invalid(getAddColumnChange("SESSION_USER", "FOO_BAR")));
    }

    @DisplayName("Should allow usage of a valid name")
    @Test
    void objectNameRuleShouldReturnFormattedErrorMessageWithCommaSeparatedMultipleErrors() {
        ruleImpl.configure(RuleConfig.builder().withPattern("^(?!_)[A-Z_0-9]+(?<!_)$").withErrorMessage("Object name '%s' must follow pattern '%s'").build());

        assertFalse(ruleImpl.invalid(getAddColumnChange("BAZ", "FOO_BAR")));
    }

    private AddColumnChange getAddColumnChange(String... columnNames) {
        AddColumnChange addColumnChange = new AddColumnChange();
        if (columnNames != null) {
            for (String columnName : columnNames) {
                AddColumnConfig addColumnConfig = new AddColumnConfig();
                addColumnConfig.setName(columnName);
                addColumnChange.getColumns().add(addColumnConfig);
            }
        }
        return addColumnChange;
    }

}
