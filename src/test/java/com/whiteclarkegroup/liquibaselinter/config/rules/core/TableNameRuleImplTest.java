package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableNameRuleImplTest {

    private TableNameRuleImpl tableNameRule;

    @BeforeEach
    void setUp() {
        tableNameRule = new TableNameRuleImpl();
    }

    @DisplayName("Table name must not be null")
    @Test
    void tableNameNameMustNotBeNull() {
        assertTrue(tableNameRule.invalid(getCreateTableChange(null)));
        assertTrue(tableNameRule.invalid(getRenameTableChange(null)));
    }

    @DisplayName("Table name must follow pattern")
    @Test
    void tableNameNameMustFollowPattern() {
        tableNameRule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build());

        assertTrue(tableNameRule.invalid(getCreateTableChange("TBL_INVALID")));
        assertTrue(tableNameRule.invalid(getRenameTableChange("TBL_INVALID")));

        assertFalse(tableNameRule.invalid(getCreateTableChange("TABLE_VALID")));
        assertFalse(tableNameRule.invalid(getRenameTableChange("TABLE_VALID")));
    }

    @DisplayName("Table name rule should support formatted error message with pattern arg")
    @Test
    void tableNameNameRuleShouldReturnFormattedErrorMessage() {
        tableNameRule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").withErrorMessage("Table name '%s' must follow pattern '%s'").build());
        assertEquals(tableNameRule.getMessage(getCreateTableChange("TBL_INVALID")), "Table name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
        assertEquals(tableNameRule.getMessage(getRenameTableChange("TBL_INVALID")), "Table name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
    }

    private CreateTableChange getCreateTableChange(String tableName) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName(tableName);
        return createTableChange;
    }

    private RenameTableChange getRenameTableChange(String tableName) {
        RenameTableChange renameTableChange = new RenameTableChange();
        renameTableChange.setNewTableName(tableName);
        return renameTableChange;
    }
}
