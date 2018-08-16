package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.change.core.CreateIndexChange;
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
class CreateIndexChangeLinterTest {

    private CreateIndexChangeLinter createIndexChangeLinter;
    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = mock(ObjectNameLinter.class);
        createIndexChangeLinter = new CreateIndexChangeLinter() {
            ObjectNameLinter getObjectNameLinter() {
                return objectNameLinter;
            }
        };
    }

    @Test
    void should_use_object_name_linter_for_name_length_check(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        CreateIndexChange validChange = new CreateIndexChange();
        validChange.setChangeSet(changeSet);
        validChange.setTableName("TEST_TEST");
        validChange.setIndexName("TEST_TEST_I1");
        changeSet.addChange(validChange);
        createIndexChangeLinter.lint(validChange, config.getRules());
        verify(objectNameLinter, times(1)).lintObjectNameLength("TEST_TEST_I1", validChange, config.getRules());
    }

    @DisplayName("Should reject name where prefix doesn't match table name")
    @Test
    void should_validate_inconsistent_with_table_name(ChangeSet changeSet, Config config) {
        CreateIndexChange validChange = new CreateIndexChange();
        validChange.setChangeSet(changeSet);
        validChange.setTableName("TEST");
        validChange.setIndexName("TEST_TEST_I1");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> createIndexChangeLinter.lint(validChange, config.getRules()));
        assertTrue(changeLogParseException.getMessage().contains("Index 'TEST_TEST_I1' must follow pattern " +
                "table name followed by 'I' and a number e.g. APPLICATION_I1, or match a primary key or unique constraint name"));
    }

    @DisplayName("Should reject name where suffix isn't one of _PK, _Un or _In")
    @Test
    void should_validate_unsuitable_suffix(ChangeSet changeSet, Config config) {
        CreateIndexChange validChange = new CreateIndexChange();
        validChange.setChangeSet(changeSet);
        validChange.setTableName("TEST_TEST");
        validChange.setIndexName("TEST_TEST_FOO");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> createIndexChangeLinter.lint(validChange, config.getRules()));
        assertTrue(changeLogParseException.getMessage().contains("Index 'TEST_TEST_FOO' must follow pattern " +
                "table name followed by 'I' and a number e.g. APPLICATION_I1, or match a primary key or unique constraint name"));
    }

    @DisplayName("Should validate name in correct format for misc index")
    @Test
    void should_validate_name_in_correct_format_misc(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        CreateIndexChange constraintChangeValid = new CreateIndexChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setIndexName("TEST_TEST_I1");
        changeSet.addChange(constraintChangeValid);
        createIndexChangeLinter.lint(constraintChangeValid, config.getRules());
    }

    @DisplayName("Should validate name in correct format for unique constraint index")
    @Test
    void should_validate_name_in_correct_format_unique(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        CreateIndexChange constraintChangeValid = new CreateIndexChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setIndexName("TEST_TEST_U1");
        changeSet.addChange(constraintChangeValid);
        createIndexChangeLinter.lint(constraintChangeValid, config.getRules());
    }

    @DisplayName("Should validate name in correct format for primary key index")
    @Test
    void should_validate_name_in_correct_format_primary(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        CreateIndexChange constraintChangeValid = new CreateIndexChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setIndexName("TEST_TEST_PK");
        changeSet.addChange(constraintChangeValid);
        createIndexChangeLinter.lint(constraintChangeValid, config.getRules());
    }

    @DisplayName("Should not validate if name in format is longer than max length")
    @Test
    void should_not_validate_if_name_in_format_more_than_max_length(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        CreateIndexChange constraintChange = new CreateIndexChange();
        constraintChange.setChangeSet(changeSet);
        constraintChange.setTableName("TEST_TEST_TEST_TEST_TEST_TEST");
        constraintChange.setIndexName("INVALID_NAME");
        changeSet.addChange(constraintChange);
        createIndexChangeLinter.lint(constraintChange, config.getRules());
    }

}
