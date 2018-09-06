package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
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
    void shouldUseObjectNameLinterForNameLengthCheck(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST");
        addPrimaryKeyChange.setConstraintName("TEST_PK");
        addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectNameLength("TEST_PK", addPrimaryKeyChange, ruleRunner);
    }

    @DisplayName("Should validate name in incorrect format")
    @Test
    void shouldValidateNameInIncorrectFormat(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST");
        addPrimaryKeyChange.setConstraintName("TEST_PK_ABC");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Primary key constraint '" + addPrimaryKeyChange.getConstraintName() + "' must end with '_PK' e.g. TABLE_PK"));
    }

    @DisplayName("Should validate name in correct format")
    @Test
    void shouldValidateNameInCorrectFormat(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST");
        addPrimaryKeyChange.setConstraintName("TEST_PK");

        try {
            addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @DisplayName("Should not validate if name in format is longer than max length")
    @Test
    void shouldNotValidateIfNameInFormatMoreThanMaxLength(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST_TEST_TEST_TEST_TEST_TEST");
        addPrimaryKeyChange.setConstraintName("INVALID_NAME_PK");
        changeSet.addChange(addPrimaryKeyChange);

        try {
            addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @DisplayName("Should not validate if name in format but validate ending when longer than max length")
    @Test
    void shouldNotValidateNameInFormatButValidateEndingWhenMoreThanMaxLength(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setChangeSet(changeSet);
        addPrimaryKeyChange.setTableName("TEST_TEST_TEST_TEST_TEST_TEST");
        addPrimaryKeyChange.setConstraintName("INVALID_NAME");
        changeSet.addChange(addPrimaryKeyChange);
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addPrimaryKeyChangeLinter.lint(addPrimaryKeyChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Primary key constraint '" + addPrimaryKeyChange.getConstraintName() + "' must " +
                "end with '_PK' e.g. TABLE_PK"));
    }

}
