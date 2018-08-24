package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.RuleRunnerParameterResolver;
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

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
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
    void shouldUseObjectNameLinterForNameLengthCheck(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddUniqueConstraintChange constraintChangeValid = new AddUniqueConstraintChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setConstraintName("TEST_TEST_U1");
        changeSet.addChange(constraintChangeValid);
        addUniqueConstraintChangeLinter.lint(constraintChangeValid, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectNameLength("TEST_TEST_U1", constraintChangeValid, ruleRunner);
    }

    @DisplayName("Should validate name in incorrect format")
    @Test
    void shouldValidateNameInIncorrectFormat(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddUniqueConstraintChange constraintChangeInvalid = new AddUniqueConstraintChange();
        constraintChangeInvalid.setChangeSet(changeSet);
        constraintChangeInvalid.setTableName("MAGIC");
        constraintChangeInvalid.setConstraintName("MAGIC");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addUniqueConstraintChangeLinter.lint(constraintChangeInvalid, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Unique constraint 'MAGIC' must follow pattern " +
                "table name followed by 'U' and a number e.g. TABLE_U1"));
    }

    @DisplayName("Should validate name in correct format")
    @Test
    void shouldValidateNameInCorrectFormat(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddUniqueConstraintChange constraintChangeValid = new AddUniqueConstraintChange();
        constraintChangeValid.setChangeSet(changeSet);
        constraintChangeValid.setTableName("TEST_TEST");
        constraintChangeValid.setConstraintName("TEST_TEST_U1");
        changeSet.addChange(constraintChangeValid);
        addUniqueConstraintChangeLinter.lint(constraintChangeValid, ruleRunner);
    }

    @DisplayName("Should not validate if name in format is longer than max length")
    @Test
    void shouldNotValidateIfNameInFormatMoreThanMaxLength(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddUniqueConstraintChange constraintChange = new AddUniqueConstraintChange();
        constraintChange.setChangeSet(changeSet);
        constraintChange.setTableName("TEST_TEST_TEST_TEST_TEST_TEST");
        constraintChange.setConstraintName("INVALID_NAME");
        changeSet.addChange(constraintChange);
        addUniqueConstraintChangeLinter.lint(constraintChange, ruleRunner);
    }

}
