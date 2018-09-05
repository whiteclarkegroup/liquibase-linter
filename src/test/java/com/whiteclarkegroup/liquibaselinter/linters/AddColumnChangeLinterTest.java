package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.AddColumnChangeParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
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
    void shouldCallColumnConfigLinter(AddColumnChange addColumnChange, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnChange.addColumn(addColumnConfig);
        addColumnChangeLinter.lint(addColumnChange, ruleRunner);
        verify(columnConfigLinter, times(1)).lintColumnConfig(addColumnChange, ruleRunner);
    }

    @Test
    void shouldUseObjectNameLinterForNameLengthCheck(AddColumnChange addColumnChange, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnChange.addColumn(addColumnConfig);
        addColumnChangeLinter.lint(addColumnChange, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectName("TEST", addColumnChange, ruleRunner);
    }
}
