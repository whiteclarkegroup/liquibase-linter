package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.RuleRunnerParameterResolver;
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
    void should_use_object_name_linter_for_name_length_check(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        RenameColumnChange renameColumnChange = new RenameColumnChange();
        renameColumnChange.setChangeSet(changeSet);
        renameColumnChange.setNewColumnName("TEST_TEST");
        changeSet.addChange(renameColumnChange);
        renameColumnChangeLinter.lint(renameColumnChange, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectName("TEST_TEST", renameColumnChange, ruleRunner);
    }
}
