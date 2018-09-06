package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.CreateTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
class TableNameLinterTest {

    private TableNameLinter tableNameLinter;

    @BeforeEach
    void setUp() {
        tableNameLinter = new TableNameLinter();
    }

    @DisplayName("Should not allow table name starting with tbl")
    @Test
    void shouldNotAllowTableNameStartingWithTbl(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TBL_TEST");
        createTableChange.setRemarks("REMARK");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> tableNameLinter.lintTableName("TBL_TEST", createTableChange, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Table 'TBL_TEST' name must be uppercase, use '_' separation and not start with TBL"));
    }

    @DisplayName("Should not allow table name exceeding max length")
    @Test
    void shouldNotAllowTableNameExceedingMaxLength(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST_TEST_TEST_TEST_TEST_TEST");
        createTableChange.setRemarks("REMARK");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> tableNameLinter.lintTableName("TEST_TEST_TEST_TEST_TEST_TEST", createTableChange, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Table 'TEST_TEST_TEST_TEST_TEST_TEST' name must not be longer than " + 26));
    }

    @DisplayName("Should not allow table name exceeding max length")
    @Test
    void shouldNotAllowLowerCaseTableName(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("test_table");
        createTableChange.setRemarks("REMARK");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> tableNameLinter.lintTableName("test_table", createTableChange, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Table 'test_table' name must be uppercase, use '_' separation and not start with TBL"));
    }


    @DisplayName("Should allow valid table name")
    @Test
    void shouldAllowValidTableName(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST_TABLE_NAME");
        createTableChange.setRemarks("REMARK");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        try {
            tableNameLinter.lintTableName("TEST_TABLE_NAME", createTableChange, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }
}
