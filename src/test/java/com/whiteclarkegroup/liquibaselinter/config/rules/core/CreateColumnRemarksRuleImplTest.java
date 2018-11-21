package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateColumnRemarksRuleImplTest {

    private CreateColumnRemarksRuleImpl createColumnRemarksRule;

    @BeforeEach
    void setUp() {
        createColumnRemarksRule = new CreateColumnRemarksRuleImpl();
    }

    @DisplayName("Should not allow create column without remarks attribute")
    @Test
    void shouldNotAllowCreateColumnWithoutRemarks() {
        assertTrue(createColumnRemarksRule.invalid(buildAddColumnChange(null)));
    }

    @DisplayName("Should not allow create column with empty attribute")
    @Test
    void shouldNotAllowCreateColumnWithEmptyRemarks() {
        assertTrue(createColumnRemarksRule.invalid(buildAddColumnChange("")));
    }

    @DisplayName("Should allow create column with remarks attribute")
    @Test
    void shouldAllowCreateColumnWithRemarks() {
        assertFalse(createColumnRemarksRule.invalid(buildAddColumnChange("Some remarks")));
    }

    private AddColumnChange buildAddColumnChange(String remarks) {
        AddColumnChange addColumnChange = new AddColumnChange();
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setRemarks(remarks);
        addColumnChange.getColumns().add(addColumnConfig);
        return addColumnChange;
    }

}
