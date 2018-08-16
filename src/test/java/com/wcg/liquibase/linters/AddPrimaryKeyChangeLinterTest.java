package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.change.core.AddPrimaryKeyChange;
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
class AddPrimaryKeyChangeLinterTest {

    private AddPrimaryKeyChangeLinter addPrimaryKeyChangeLinter;
    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = mock(ObjectNameLinter.class);
        addPrimaryKeyChangeLinter = new AddPrimaryKeyChangeLinter() {
            ObjectNameLinter getObjectNameLinter() {
                return objectNameLinter;
            }
        };
    }

    @Test
    void should_use_object_name_linter_for_name_length_check(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST");
        addPrimaryKeyChange.setConstraintName("TEST_PK");
        addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, config.getRules());
        verify(objectNameLinter, times(1)).lintObjectNameLength("TEST_PK", addPrimaryKeyChange, config.getRules());
    }

    @DisplayName("Should validate name in incorrect format")
    @Test
    void should_validate_name_in_incorrect_format(ChangeSet changeSet, Config config) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST");
        addPrimaryKeyChange.setConstraintName("TEST_PK_ABC");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, config.getRules()));
        assertTrue(changeLogParseException.getMessage().contains("Primary key constraint '" + addPrimaryKeyChange.getConstraintName() + "' must end with '_PK' e.g. TABLE_PK"));
    }

    @DisplayName("Should validate name in correct format")
    @Test
    void should_validate_name_in_correct_format(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST");
        addPrimaryKeyChange.setConstraintName("TEST_PK");
        addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, config.getRules());
    }

    @DisplayName("Should not validate if name in format is longer than max length")
    @Test
    void should_not_validate_if_name_in_format_more_than_max_length(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST_TEST_TEST_TEST_TEST_TEST");
        addPrimaryKeyChange.setConstraintName("INVALID_NAME_PK");
        changeSet.addChange(addPrimaryKeyChange);
        addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, config.getRules());
    }

    @DisplayName("Should not validate if name in format but validate ending when longer than max length")
    @Test
    void should_not_validate_name_in_format_but_validate_ending_when_more_than_max_length(ChangeSet changeSet, Config config) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST_TEST_TEST_TEST_TEST_TEST");
        addPrimaryKeyChange.setConstraintName("INVALID_NAME");
        changeSet.addChange(addPrimaryKeyChange);
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, config.getRules()));
        assertTrue(changeLogParseException.getMessage().contains("Primary key constraint '" + addPrimaryKeyChange.getConstraintName() + "' must " +
                "end with '_PK' e.g. TABLE_PK"));
    }

}
