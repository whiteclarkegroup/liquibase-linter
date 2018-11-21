package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.core.ObjectNameRulesImpl.ObjectNameLengthRuleImpl;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectNameLengthRuleImplTest {

    private ObjectNameLengthRuleImpl objectNameLengthRule;

    @BeforeEach
    void setUp() {
        objectNameLengthRule = new ObjectNameLengthRuleImpl();
    }

    @DisplayName("Object name must not exceed max length")
    @Test
    void objectNameMustNotExceedMaxLength() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).build());
        assertTrue(objectNameLengthRule.invalid(getAddColumnChange("VALUE")));
        assertTrue(objectNameLengthRule.invalid(getAddForeignKeyConstraintChange("VALUE")));
        assertTrue(objectNameLengthRule.invalid(getAddPrimaryKeyConstraintChange("VALUE")));
        assertTrue(objectNameLengthRule.invalid(getAddUniqueConstraintChange("VALUE")));
        assertTrue(objectNameLengthRule.invalid(getCreateTableChange("VALUE")));
        assertTrue(objectNameLengthRule.invalid(getMergeColumnChange("VALUE")));
        assertTrue(objectNameLengthRule.invalid(getRenameColumnChange("VALUE")));
        assertTrue(objectNameLengthRule.invalid(getRenameViewChange("VALUE")));
        assertTrue(objectNameLengthRule.invalid(getCreateViewChange("VALUE")));
        assertTrue(objectNameLengthRule.invalid(getCreateIndexChange("VALUE")));
    }

    @DisplayName("Object name can equal max length")
    @Test
    void tableLengthCanEqualMaxLength() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(5).build());
        assertFalse(objectNameLengthRule.invalid(getAddColumnChange("VALUE")));
        assertFalse(objectNameLengthRule.invalid(getAddForeignKeyConstraintChange("VALUE")));
        assertFalse(objectNameLengthRule.invalid(getAddPrimaryKeyConstraintChange("VALUE")));
        assertFalse(objectNameLengthRule.invalid(getAddUniqueConstraintChange("VALUE")));
        assertFalse(objectNameLengthRule.invalid(getCreateTableChange("VALUE")));
        assertFalse(objectNameLengthRule.invalid(getMergeColumnChange("VALUE")));
        assertFalse(objectNameLengthRule.invalid(getRenameColumnChange("VALUE")));
        assertFalse(objectNameLengthRule.invalid(getRenameViewChange("VALUE")));
        assertFalse(objectNameLengthRule.invalid(getCreateViewChange("VALUE")));
        assertFalse(objectNameLengthRule.invalid(getCreateIndexChange("VALUE")));
    }

    @DisplayName("Object name can be null")
    @Test
    void objectNameCanBeNull() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).build());
        assertFalse(objectNameLengthRule.invalid(getAddColumnChange(null)));
        assertFalse(objectNameLengthRule.invalid(getAddForeignKeyConstraintChange(null)));
        assertFalse(objectNameLengthRule.invalid(getAddPrimaryKeyConstraintChange(null)));
        assertFalse(objectNameLengthRule.invalid(getAddUniqueConstraintChange(null)));
        assertFalse(objectNameLengthRule.invalid(getCreateTableChange(null)));
        assertFalse(objectNameLengthRule.invalid(getMergeColumnChange(null)));
        assertFalse(objectNameLengthRule.invalid(getRenameColumnChange(null)));
        assertFalse(objectNameLengthRule.invalid(getRenameViewChange(null)));
        assertFalse(objectNameLengthRule.invalid(getCreateViewChange(null)));
        assertFalse(objectNameLengthRule.invalid(getCreateIndexChange(null)));
    }

    @DisplayName("Object name length rule should support formatted error message with length arg")
    @Test
    void objectNameLengthRuleShouldReturnFormattedErrorMessage() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).withErrorMessage("Object name '%s' must be less than %d characters").build());

        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getAddColumnChange("VALUE")));
        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getAddForeignKeyConstraintChange("VALUE")));
        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getAddPrimaryKeyConstraintChange("VALUE")));
        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getAddUniqueConstraintChange("VALUE")));
        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getCreateTableChange("VALUE")));
        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getMergeColumnChange("VALUE")));
        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getRenameColumnChange("VALUE")));
        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getRenameViewChange("VALUE")));
        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getCreateViewChange("VALUE")));
        assertEquals("Object name 'VALUE' must be less than 4 characters", objectNameLengthRule.getMessage(getCreateIndexChange("VALUE")));
    }

    @DisplayName("Object name length rule should support formatted error message with comma separated multiple errors")
    @Test
    void objectNameLengthRuleShouldReturnFormattedErrorMessageWithCommaSeparatedMultipleErrors() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).withErrorMessage("Object name '%s' must be less than %d characters").build());

        assertEquals("Object name 'VALUE,VALUE2' must be less than 4 characters", objectNameLengthRule.getMessage(getAddColumnChange("VALUE", "VALUE2")));

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

    private AddForeignKeyConstraintChange getAddForeignKeyConstraintChange(String constraintName) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setConstraintName(constraintName);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCED");
        return addForeignKeyConstraintChange;
    }

    private AddPrimaryKeyChange getAddPrimaryKeyConstraintChange(String constraintName) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setConstraintName(constraintName);
        addPrimaryKeyChange.setTableName("VALUE");
        return addPrimaryKeyChange;
    }

    private AddUniqueConstraintChange getAddUniqueConstraintChange(String constraintName) {
        AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
        addUniqueConstraintChange.setConstraintName(constraintName);
        return addUniqueConstraintChange;
    }

    private CreateTableChange getCreateTableChange(String columnName) {
        CreateTableChange createTableChange = new CreateTableChange();
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName(columnName);
        createTableChange.getColumns().add(addColumnConfig);
        return createTableChange;
    }

    private MergeColumnChange getMergeColumnChange(String columnName) {
        MergeColumnChange mergeColumnChange = new MergeColumnChange();
        mergeColumnChange.setFinalColumnName(columnName);
        return mergeColumnChange;
    }

    private RenameColumnChange getRenameColumnChange(String columnName) {
        RenameColumnChange renameColumnChange = new RenameColumnChange();
        renameColumnChange.setNewColumnName(columnName);
        return renameColumnChange;
    }

    private RenameViewChange getRenameViewChange(String viewName) {
        RenameViewChange renameViewChange = new RenameViewChange();
        renameViewChange.setNewViewName(viewName);
        return renameViewChange;
    }

    private CreateViewChange getCreateViewChange(String viewName) {
        CreateViewChange createViewChange = new CreateViewChange();
        createViewChange.setViewName(viewName);
        return createViewChange;
    }

    private CreateIndexChange getCreateIndexChange(String indexName) {
        CreateIndexChange createViewChange = new CreateIndexChange();
        createViewChange.setIndexName(indexName);
        return createViewChange;
    }
}
