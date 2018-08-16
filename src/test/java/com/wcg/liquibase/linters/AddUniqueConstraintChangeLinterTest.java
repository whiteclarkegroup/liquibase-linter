package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.change.core.AddUniqueConstraintChange;
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
class AddUniqueConstraintChangeLinterTest {

    private AddUniqueConstraintChangeLinter addUniqueConstraintChangeLinter;
    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = mock(ObjectNameLinter.class);
        addUniqueConstraintChangeLinter = new AddUniqueConstraintChangeLinter() {
            ObjectNameLinter getObjectNameLinter() {
                return objectNameLinter;
            }
        };
    }

    @Test
    void should_use_object_name_linter_for_name_length_check(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        AddUniqueConstraintChange constraintChangeValid = new AddUniqueConstraintChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setConstraintName("TEST_TEST_U1");
        changeSet.addChange(constraintChangeValid);
        addUniqueConstraintChangeLinter.lint(constraintChangeValid, config.getRules());
        verify(objectNameLinter, times(1)).lintObjectNameLength("TEST_TEST_U1", constraintChangeValid, config.getRules());
    }

    @DisplayName("Should validate name in incorrect format")
    @Test
    void should_validate_name_in_incorrect_format(ChangeSet changeSet, Config config) {
        AddUniqueConstraintChange constraintChangeInvalid = new AddUniqueConstraintChange();
        constraintChangeInvalid.setChangeSet(changeSet);
        constraintChangeInvalid.setTableName("MAGIC");
        constraintChangeInvalid.setConstraintName("MAGIC");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addUniqueConstraintChangeLinter.lint(constraintChangeInvalid, config.getRules()));
        assertTrue(changeLogParseException.getMessage().contains("Unique constraint 'MAGIC' must follow pattern " +
                "table name followed by 'U' and a number e.g. TABLE_U1"));
    }

    @DisplayName("Should validate name in correct format")
    @Test
    void should_validate_name_in_correct_format(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        AddUniqueConstraintChange constraintChangeValid = new AddUniqueConstraintChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setConstraintName("TEST_TEST_U1");
        changeSet.addChange(constraintChangeValid);
        addUniqueConstraintChangeLinter.lint(constraintChangeValid, config.getRules());
    }

    @DisplayName("Should not validate if name in format is longer than max length")
    @Test
    void should_not_validate_if_name_in_format_more_than_max_length(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        AddUniqueConstraintChange constraintChange = new AddUniqueConstraintChange();
        constraintChange.setChangeSet(changeSet);
        constraintChange.setTableName("TEST_TEST_TEST_TEST_TEST_TEST");
        constraintChange.setConstraintName("INVALID_NAME");
        changeSet.addChange(constraintChange);
        addUniqueConstraintChangeLinter.lint(constraintChange, config.getRules());
    }

}
