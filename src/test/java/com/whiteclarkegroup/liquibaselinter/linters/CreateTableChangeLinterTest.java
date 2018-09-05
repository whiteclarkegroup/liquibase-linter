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
import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
class CreateTableChangeLinterTest {

    private CreateTableChangeLinter createTableChangeLinter;
    private ColumnConfigLinter columnConfigLinter;
    private TableNameLinter tableNameLinter;

    @BeforeEach
    void setUp() {
        tableNameLinter = mock(TableNameLinter.class);
        columnConfigLinter = mock(ColumnConfigLinter.class);
        createTableChangeLinter = new CreateTableChangeLinter() {
            public ColumnConfigLinter getColumnConfigLinter() {
                return columnConfigLinter;
            }

            public TableNameLinter getTableNameLinter() {
                return tableNameLinter;
            }
        };
    }

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowAddColumnWithoutRemarks(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> createTableChangeLinter.lint(createTableChange, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Create table must contain remark attribute"));

    }

    @DisplayName("Should allow create table with remarks attribute")
    @Test
    void shouldAllowAddColumnWithRemarks(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setRemarks("REMARK");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);
        try {
            createTableChangeLinter.lint(createTableChange, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @Test
    void shouldCallTableNameLinter(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setRemarks("REMARK");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        createTableChangeLinter.lint(createTableChange, ruleRunner);
        verify(tableNameLinter, times(1)).lintTableName("TEST", createTableChange, ruleRunner);
    }
}
