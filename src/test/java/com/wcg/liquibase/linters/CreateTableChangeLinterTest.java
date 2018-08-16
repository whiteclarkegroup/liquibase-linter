package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.change.core.CreateTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, DefaultConfigParameterResolver.class})
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
    void should_not_allow_add_column_without_remarks(ChangeSet changeSet, Config config) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> createTableChangeLinter.lint(createTableChange, config.getRules()));

        assertTrue(changeLogParseException.getMessage().contains("Create table must contain remark attribute"));

    }

    @DisplayName("Should allow create table with remarks attribute")
    @Test
    void should_allow_add_column_with_remarks(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setRemarks("REMARK");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        createTableChangeLinter.lint(createTableChange, config.getRules());
    }

    @Test
    void should_call_table_name_linter(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setRemarks("REMARK");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        createTableChangeLinter.lint(createTableChange, config.getRules());
        verify(tableNameLinter, times(1)).lintTableName("TEST", createTableChange, config.getRules());
    }
}
