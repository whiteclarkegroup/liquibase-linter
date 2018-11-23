package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.core.ObjectNameRulesImpl.ObjectNameRuleImpl;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectNameRuleImplTest {

    private ObjectNameRuleImpl objectNameRule;

    @BeforeEach
    void setUp() {
        objectNameRule = new ObjectNameRuleImpl();
    }

    @DisplayName("Object name must not be null")
    @Test
    void objectNameMustNotBeNull() {
        objectNameRule.configure(RuleConfig.builder().withPattern("^(?!_)[A-Z_0-9]+(?<!_)$").build());
        assertTrue(objectNameRule.invalid(getAddColumnChange(new String[]{null})));
        assertTrue(objectNameRule.invalid(getAddForeignKeyConstraintChange(null)));
        assertTrue(objectNameRule.invalid(getAddPrimaryKeyConstraintChange(null)));
        assertTrue(objectNameRule.invalid(getAddUniqueConstraintChange(null)));
        assertTrue(objectNameRule.invalid(getCreateTableChange(null)));
        assertTrue(objectNameRule.invalid(getMergeColumnChange(null)));
        assertTrue(objectNameRule.invalid(getRenameColumnChange(null)));
        assertTrue(objectNameRule.invalid(getRenameViewChange(null)));
        assertTrue(objectNameRule.invalid(getCreateViewChange(null)));
        assertTrue(objectNameRule.invalid(getCreateIndexChange(null)));
    }

    @DisplayName("Object name length rule should support formatted error message with length arg")
    @Test
    void objectNameRuleShouldReturnFormattedErrorMessage() {
        objectNameRule.configure(RuleConfig.builder().withPattern("^(?!_)[A-Z_0-9]+(?<!_)$").withErrorMessage("Object name '%s' must follow pattern '%s'").build());

        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getAddColumnChange("&VALUE")));
        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getAddForeignKeyConstraintChange("&VALUE")));
        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getAddPrimaryKeyConstraintChange("&VALUE")));
        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getAddUniqueConstraintChange("&VALUE")));
        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getCreateTableChange("&VALUE")));
        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getMergeColumnChange("&VALUE")));
        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getRenameColumnChange("&VALUE")));
        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getRenameViewChange("&VALUE")));
        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getCreateViewChange("&VALUE")));
        assertEquals("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getCreateIndexChange("&VALUE")));
    }

    @DisplayName("Object name length rule should support formatted error message with comma separated multiple errors")
    @Test
    void objectNameRuleShouldReturnFormattedErrorMessageWithCommaSeparatedMultipleErrors() {
        objectNameRule.configure(RuleConfig.builder().withPattern("^(?!_)[A-Z_0-9]+(?<!_)$").withErrorMessage("Object name '%s' must follow pattern '%s'").build());

        assertEquals("Object name '&VALUE,&VALUE2' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'", objectNameRule.getMessage(getAddColumnChange("&VALUE", "&VALUE2")));

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
