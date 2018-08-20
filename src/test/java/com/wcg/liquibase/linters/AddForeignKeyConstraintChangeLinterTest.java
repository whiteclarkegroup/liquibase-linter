package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.AddForeignKeyConstraintChange;
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
class AddForeignKeyConstraintChangeLinterTest {

    private AddForeignKeyConstraintChangeLinter addForeignKeyConstraintChangeLinter;
    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = mock(ObjectNameLinter.class);
        addForeignKeyConstraintChangeLinter = new AddForeignKeyConstraintChangeLinter() {
            ObjectNameLinter getObjectNameLinter() {
                return objectNameLinter;
            }
        };
    }

    @Test
    void should_use_object_name_linter_for_name_length_check(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setChangeSet(changeSet);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCE");
        addForeignKeyConstraintChange.setConstraintName("BASE_REFERENCE_FK");
        addForeignKeyConstraintChangeLinter.lint(addForeignKeyConstraintChange, ruleRunner);
        verify(objectNameLinter, times(1)).lintObjectNameLength("BASE_REFERENCE_FK", addForeignKeyConstraintChange, ruleRunner);
    }

    @DisplayName("Should validate name in incorrect format")
    @Test
    void should_validate_name_in_incorrect_format(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setChangeSet(changeSet);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCE");
        addForeignKeyConstraintChange.setConstraintName("TEST_PK_ABC");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addForeignKeyConstraintChangeLinter.lint(addForeignKeyConstraintChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Foreign key constraint 'TEST_PK_ABC' must end with '_FK'. e.g. ORDER_CUSTOMER_FK"));
    }

    @DisplayName("Should validate name in correct format")
    @Test
    void should_validate_name_in_correct_format(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setChangeSet(changeSet);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCE");
        addForeignKeyConstraintChange.setConstraintName("BASE_REFERENCE_FK");
        addForeignKeyConstraintChangeLinter.lint(addForeignKeyConstraintChange, ruleRunner);
    }

    @DisplayName("Should not validate if name in format is longer than max length")
    @Test
    void should_not_validate_if_name_in_format_more_than_max_length(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setChangeSet(changeSet);
        addForeignKeyConstraintChange.setBaseTableName("TEST_TEST_TEST_TEST_TEST");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCE");
        addForeignKeyConstraintChange.setConstraintName("TEST_PK_ABC_FK");
        addForeignKeyConstraintChangeLinter.lint(addForeignKeyConstraintChange, ruleRunner);
    }

    @DisplayName("Should not validate if name in format but validate ending when longer than max length")
    @Test
    void should_not_validate_name_in_format_but_validate_ending_when_more_than_max_length(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setChangeSet(changeSet);
        addForeignKeyConstraintChange.setBaseTableName("TEST_TEST_TEST_TEST_TEST");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCE");
        addForeignKeyConstraintChange.setConstraintName("TEST_PK_ABC");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addForeignKeyConstraintChangeLinter.lint(addForeignKeyConstraintChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Foreign key constraint '" + addForeignKeyConstraintChange.getConstraintName() + "' must " +
                "end with '_FK'. e.g. ORDER_CUSTOMER_FK"));
    }
}
