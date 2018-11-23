package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import liquibase.change.core.CreateTableChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateTableRemarksRuleImplTest {

    private CreateTableRemarksRuleImpl createTableRemarksRule;

    @BeforeEach
    void setUp() {
        createTableRemarksRule = new CreateTableRemarksRuleImpl();
    }

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowCreteTableWithoutRemarks() {
        assertTrue(createTableRemarksRule.invalid(getCreateTableChange(null)));
    }

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowCreateTableWithEmptyRemarks() {
        assertTrue(createTableRemarksRule.invalid(getCreateTableChange("")));
    }

    @DisplayName("Should allow create table with remarks attribute")
    @Test
    void shouldAllowCreateTableWithRemarks() {
        assertFalse(createTableRemarksRule.invalid(getCreateTableChange("REMARK")));
    }

    private CreateTableChange getCreateTableChange(String remarks) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setRemarks(remarks);
        return createTableChange;
    }

}
