package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.RenameTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
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
    void shouldCallTableNameLinter(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        RenameTableChange renameTableChange = new RenameTableChange();
        renameTableChange.setNewTableName("TEST");
        renameTableChange.setChangeSet(changeSet);
        changeSet.addChange(renameTableChange);

        renameTableChangeLinter.lint(renameTableChange, ruleRunner);
        verify(tableNameLinter, times(1)).lintTableName("TEST", renameTableChange, ruleRunner);
    }

}
