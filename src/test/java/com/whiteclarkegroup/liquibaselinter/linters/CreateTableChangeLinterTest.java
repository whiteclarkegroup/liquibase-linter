package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.CreateTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
class CreateTableChangeLinterTest {

    private CreateTableChangeLinter createTableChangeLinter;
    private TableNameLinter tableNameLinter;

    @BeforeEach
    void setUp() {
        tableNameLinter = mock(TableNameLinter.class);
        createTableChangeLinter = new CreateTableChangeLinter() {

            public TableNameLinter getTableNameLinter() {
                return tableNameLinter;
            }
        };
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
