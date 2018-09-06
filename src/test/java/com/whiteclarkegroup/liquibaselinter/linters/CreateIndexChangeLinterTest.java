package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.CreateIndexChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
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
    void shouldUseObjectNameLinterForNameLengthCheck(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        CreateIndexChange validChange = new CreateIndexChange();
        validChange.setChangeSet(changeSet);
        validChange.setTableName("TEST_TEST");
        validChange.setIndexName("TEST_TEST_I1");
        changeSet.addChange(validChange);
        createIndexChangeLinter.lint(validChange, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectNameLength("TEST_TEST_I1", validChange, ruleRunner);
    }

    @DisplayName("Should reject name where prefix doesn't match table name")
    @Test
    void shouldValidateInconsistentWithTableName(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateIndexChange validChange = new CreateIndexChange();
        validChange.setChangeSet(changeSet);
        validChange.setTableName("TEST");
        validChange.setIndexName("TEST_TEST_I1");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> createIndexChangeLinter.lint(validChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Index 'TEST_TEST_I1' must follow pattern " +
                "table name followed by 'I' and a number e.g. APPLICATION_I1, or match a primary key or unique constraint name"));
    }

    @DisplayName("Should reject name where suffix isn't one of _PK, _Un or _In")
    @Test
    void shouldValidateUnsuitableSuffix(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateIndexChange validChange = new CreateIndexChange();
        validChange.setChangeSet(changeSet);
        validChange.setTableName("TEST_TEST");
        validChange.setIndexName("TEST_TEST_FOO");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> createIndexChangeLinter.lint(validChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Index 'TEST_TEST_FOO' must follow pattern " +
                "table name followed by 'I' and a number e.g. APPLICATION_I1, or match a primary key or unique constraint name"));
    }

    @DisplayName("Should validate name in correct format for misc index")
    @Test
    void shouldValidateNameInCorrectFormatMisc(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateIndexChange constraintChangeValid = new CreateIndexChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setIndexName("TEST_TEST_I1");
        changeSet.addChange(constraintChangeValid);
        try {
            createIndexChangeLinter.lint(constraintChangeValid, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @DisplayName("Should validate name in correct format for unique constraint index")
    @Test
    void shouldValidateNameInCorrectFormatUnique(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateIndexChange constraintChangeValid = new CreateIndexChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setIndexName("TEST_TEST_U1");
        changeSet.addChange(constraintChangeValid);
        try {
            createIndexChangeLinter.lint(constraintChangeValid, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @DisplayName("Should validate name in correct format for primary key index")
    @Test
    void shouldValidateNameInCorrectFormatPrimary(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateIndexChange constraintChangeValid = new CreateIndexChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setIndexName("TEST_TEST_PK");
        changeSet.addChange(constraintChangeValid);
        try {
            createIndexChangeLinter.lint(constraintChangeValid, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @DisplayName("Should not validate if name in format is longer than max length")
    @Test
    void shouldNotValidateIfNameInFormatMoreThanMaxLength(ChangeSet changeSet, RuleRunner ruleRunner) {
        CreateIndexChange constraintChange = new CreateIndexChange();
        constraintChange.setChangeSet(changeSet);
        constraintChange.setTableName("TEST_TEST_TEST_TEST_TEST_TEST");
        constraintChange.setIndexName("INVALID_NAME");
        changeSet.addChange(constraintChange);
        try {
            createIndexChangeLinter.lint(constraintChange, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

}
