package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.change.core.RenameTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, DefaultConfigParameterResolver.class})
class RenameTableChangeLinterTest {

    private RenameTableChangeLinter renameTableChangeLinter;
    private TableNameLinter tableNameLinter;

    @BeforeEach
    void setUp() {
        tableNameLinter = mock(TableNameLinter.class);
        renameTableChangeLinter = new RenameTableChangeLinter() {
            public TableNameLinter getTableNameLinter() {
                return tableNameLinter;
            }
        };
    }

    @Test
    void should_call_table_name_linter(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        RenameTableChange renameTableChange = new RenameTableChange();
        renameTableChange.setNewTableName("TEST");
        renameTableChange.setChangeSet(changeSet);
        changeSet.addChange(renameTableChange);

        renameTableChangeLinter.lint(renameTableChange, config.getRules());
        verify(tableNameLinter, times(1)).lintTableName("TEST", renameTableChange, config.getRules());
    }

}
