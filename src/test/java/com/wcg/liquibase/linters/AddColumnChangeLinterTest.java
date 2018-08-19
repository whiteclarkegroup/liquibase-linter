package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.resolvers.AddColumnChangeParameterResolver;
import com.wcg.liquibase.resolvers.RuleRunnerParameterResolver;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith({AddColumnChangeParameterResolver.class, RuleRunnerParameterResolver.class})
class AddColumnChangeLinterTest {

    private AddColumnChangeLinter addColumnChangeLinter;
    private ColumnConfigLinter columnConfigLinter;
    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        columnConfigLinter = mock(ColumnConfigLinter.class);
        objectNameLinter = mock(ObjectNameLinter.class);
        addColumnChangeLinter = new AddColumnChangeLinter() {
            @Override
            public ColumnConfigLinter getColumnConfigLinter() {
                return columnConfigLinter;
            }

            @Override
            public ObjectNameLinter getObjectNameLinter() {
                return objectNameLinter;
            }
        };
    }

    @DisplayName("Should call column config linter")
    @Test
    void should_call_column_config_linter(AddColumnChange addColumnChange, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnChange.addColumn(addColumnConfig);
        addColumnChangeLinter.lint(addColumnChange, ruleRunner);
        verify(columnConfigLinter, times(1)).lintColumnConfig(addColumnChange, ruleRunner);
    }

    @Test
    void should_use_object_name_linter_for_name_length_check(AddColumnChange addColumnChange, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnChange.addColumn(addColumnConfig);
        addColumnChangeLinter.lint(addColumnChange, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectName("TEST", addColumnChange, ruleRunner);
    }
}
