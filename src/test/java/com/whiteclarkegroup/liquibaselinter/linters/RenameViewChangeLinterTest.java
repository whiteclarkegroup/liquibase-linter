package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.RenameViewChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
class RenameViewChangeLinterTest {

    private RenameViewChangeLinter renameViewChangeLinter;
    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = mock(ObjectNameLinter.class);
        renameViewChangeLinter = new RenameViewChangeLinter() {
            ObjectNameLinter getObjectNameLinter() {
                return objectNameLinter;
            }
        };
    }

    @Test
    void shouldUseObjectNameLinterForNameLengthCheck(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        RenameViewChange renameViewChange = new RenameViewChange();
        renameViewChange.setChangeSet(changeSet);
        renameViewChange.setNewViewName("TEST_TEST");
        changeSet.addChange(renameViewChange);
        renameViewChangeLinter.lint(renameViewChange, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectNameLength("TEST_TEST", renameViewChange, ruleRunner);
    }
}
