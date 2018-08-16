package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.change.core.RenameViewChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, DefaultConfigParameterResolver.class})
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
    void should_use_object_name_linter_for_name_length_check(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        RenameViewChange renameViewChange = new RenameViewChange();
        renameViewChange.setChangeSet(changeSet);
        renameViewChange.setNewViewName("TEST_TEST");
        changeSet.addChange(renameViewChange);
        renameViewChangeLinter.lint(renameViewChange, config.getRules());
        verify(objectNameLinter, times(1)).lintObjectNameLength("TEST_TEST", renameViewChange, config.getRules());
    }
}
