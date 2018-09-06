package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.RenameColumnChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
class RenameColumnChangeLinterTest {

    private RenameColumnChangeLinter renameColumnChangeLinter;
    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = mock(ObjectNameLinter.class);
        renameColumnChangeLinter = new RenameColumnChangeLinter() {
            public ObjectNameLinter getObjectNameLinter() {
                return objectNameLinter;
            }
        };
    }

    @Test
    void shouldUseObjectNameLinterForNameLengthCheck(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        RenameColumnChange renameColumnChange = new RenameColumnChange();
        renameColumnChange.setChangeSet(changeSet);
        renameColumnChange.setNewColumnName("TEST_TEST");
        changeSet.addChange(renameColumnChange);
        renameColumnChangeLinter.lint(renameColumnChange, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectName("TEST_TEST", renameColumnChange, ruleRunner);
    }
}
