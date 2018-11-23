package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.core.SchemaNameRulesImpl.NoSchemaNameRuleImpl;
import liquibase.change.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoSchemaNameRuleImplTest {

    private NoSchemaNameRuleImpl noSchemaNameRule;

    @BeforeEach
    void setUp() {
        noSchemaNameRule = new NoSchemaNameRuleImpl();
    }

    @DisplayName("Schema name should be null")
    @Test
    void schemaNameShouldBeNull() {
        assertTrue(noSchemaNameRule.invalid(getAddColumnChange("SCHEMA_NAME")));
        assertTrue(noSchemaNameRule.invalid(getAddForeignKeyConstraintChange("SCHEMA_NAME", "SCHEMA_NAME")));
        assertTrue(noSchemaNameRule.invalid(getAddPrimaryKeyConstraintChange("SCHEMA_NAME")));
        assertTrue(noSchemaNameRule.invalid(getAddUniqueConstraintChange("SCHEMA_NAME")));
        assertTrue(noSchemaNameRule.invalid(getCreateTableChange("SCHEMA_NAME")));
        assertTrue(noSchemaNameRule.invalid(getMergeColumnChange("SCHEMA_NAME")));
        assertTrue(noSchemaNameRule.invalid(getRenameColumnChange("SCHEMA_NAME")));
        assertTrue(noSchemaNameRule.invalid(getRenameViewChange("SCHEMA_NAME")));
        assertTrue(noSchemaNameRule.invalid(getCreateViewChange("SCHEMA_NAME")));
        assertTrue(noSchemaNameRule.invalid(getCreateIndexChange("SCHEMA_NAME")));
    }

    @DisplayName("Schema name null should be valid")
    @Test
    void schemaNameNullShouldBeValid() {
        assertFalse(noSchemaNameRule.invalid(getAddColumnChange(null)));
        assertFalse(noSchemaNameRule.invalid(getAddForeignKeyConstraintChange(null, null)));
        assertFalse(noSchemaNameRule.invalid(getAddPrimaryKeyConstraintChange(null)));
        assertFalse(noSchemaNameRule.invalid(getAddUniqueConstraintChange(null)));
        assertFalse(noSchemaNameRule.invalid(getCreateTableChange(null)));
        assertFalse(noSchemaNameRule.invalid(getMergeColumnChange(null)));
        assertFalse(noSchemaNameRule.invalid(getRenameColumnChange(null)));
        assertFalse(noSchemaNameRule.invalid(getRenameViewChange(null)));
        assertFalse(noSchemaNameRule.invalid(getCreateViewChange(null)));
        assertFalse(noSchemaNameRule.invalid(getCreateIndexChange(null)));
    }

    @DisplayName("Schema name empty should be valid")
    @Test
    void schemaNameEmptyShouldBeValid() {
        assertFalse(noSchemaNameRule.invalid(getAddColumnChange("")));
        assertFalse(noSchemaNameRule.invalid(getAddForeignKeyConstraintChange("", "")));
        assertFalse(noSchemaNameRule.invalid(getAddPrimaryKeyConstraintChange("")));
        assertFalse(noSchemaNameRule.invalid(getAddUniqueConstraintChange("")));
        assertFalse(noSchemaNameRule.invalid(getCreateTableChange("")));
        assertFalse(noSchemaNameRule.invalid(getMergeColumnChange("")));
        assertFalse(noSchemaNameRule.invalid(getRenameColumnChange("")));
        assertFalse(noSchemaNameRule.invalid(getRenameViewChange("")));
        assertFalse(noSchemaNameRule.invalid(getCreateViewChange("")));
        assertFalse(noSchemaNameRule.invalid(getCreateIndexChange("")));
    }

    private AddColumnChange getAddColumnChange(String schemaName) {
        AddColumnChange addColumnChange = new AddColumnChange();
        addColumnChange.setSchemaName(schemaName);
        return addColumnChange;
    }

    private AddForeignKeyConstraintChange getAddForeignKeyConstraintChange(String baseSchemaName, String referenceSchemaName) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setBaseTableSchemaName(baseSchemaName);
        addForeignKeyConstraintChange.setReferencedTableSchemaName(referenceSchemaName);
        return addForeignKeyConstraintChange;
    }

    private AddPrimaryKeyChange getAddPrimaryKeyConstraintChange(String schemaName) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setSchemaName(schemaName);
        return addPrimaryKeyChange;
    }

    private AddUniqueConstraintChange getAddUniqueConstraintChange(String schemaName) {
        AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
        addUniqueConstraintChange.setSchemaName(schemaName);
        return addUniqueConstraintChange;
    }

    private CreateTableChange getCreateTableChange(String schemaName) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setSchemaName(schemaName);
        return createTableChange;
    }

    private MergeColumnChange getMergeColumnChange(String schemaName) {
        MergeColumnChange mergeColumnChange = new MergeColumnChange();
        mergeColumnChange.setSchemaName(schemaName);
        return mergeColumnChange;
    }

    private RenameColumnChange getRenameColumnChange(String schemaName) {
        RenameColumnChange renameColumnChange = new RenameColumnChange();
        renameColumnChange.setSchemaName(schemaName);
        return renameColumnChange;
    }

    private RenameViewChange getRenameViewChange(String schemaName) {
        RenameViewChange renameViewChange = new RenameViewChange();
        renameViewChange.setSchemaName(schemaName);
        return renameViewChange;
    }

    private CreateViewChange getCreateViewChange(String schemaName) {
        CreateViewChange createViewChange = new CreateViewChange();
        createViewChange.setSchemaName(schemaName);
        return createViewChange;
    }

    private CreateIndexChange getCreateIndexChange(String schemaName) {
        CreateIndexChange createViewChange = new CreateIndexChange();
        createViewChange.setSchemaName(schemaName);
        return createViewChange;
    }
}
