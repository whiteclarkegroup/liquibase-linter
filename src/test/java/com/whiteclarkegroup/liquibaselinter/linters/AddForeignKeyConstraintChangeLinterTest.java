package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
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
    void shouldUseObjectNameLinterForNameLengthCheck(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
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
    void shouldValidateNameInIncorrectFormat(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setChangeSet(changeSet);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCE");
        addForeignKeyConstraintChange.setConstraintName("TEST_PK_ABC");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addForeignKeyConstraintChangeLinter.lint(addForeignKeyConstraintChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Foreign key constraint 'TEST_PK_ABC' must follow pattern {base_table_name}_{parent_table_name}_FK. e.g. ORDER_CUSTOMER_FK"));
    }

    @DisplayName("Should validate name in correct format")
    @Test
    void shouldValidateNameInCorrectFormat(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setChangeSet(changeSet);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCE");
        addForeignKeyConstraintChange.setConstraintName("BASE_REFERENCE_FK");

        try {
            addForeignKeyConstraintChangeLinter.lint(addForeignKeyConstraintChange, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @DisplayName("Should not validate if name in format is longer than max length")
    @Test
    void shouldNotValidateIfNameInFormatMoreThanMaxLength(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setChangeSet(changeSet);
        addForeignKeyConstraintChange.setBaseTableName("TEST_TEST_TEST_TEST_TEST");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCE");
        addForeignKeyConstraintChange.setConstraintName("TEST_PK_ABC_FK");

        try {
            addForeignKeyConstraintChangeLinter.lint(addForeignKeyConstraintChange, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @DisplayName("Should not validate if name in format but validate ending when longer than max length")
    @Test
    void shouldNotValidateNameInFormatButValidateEndingWhenMoreThanMaxLength(ChangeSet changeSet, RuleRunner ruleRunner) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setChangeSet(changeSet);
        addForeignKeyConstraintChange.setBaseTableName("TEST_TEST_TEST_TEST_TEST");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCE");
        addForeignKeyConstraintChange.setConstraintName("TEST_PK_ABC");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> addForeignKeyConstraintChangeLinter.lint(addForeignKeyConstraintChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Foreign key constraint '" + addForeignKeyConstraintChange.getConstraintName() +
                "' must follow pattern {base_table_name}_{parent_table_name}_FK. e.g. ORDER_CUSTOMER_FK"));
    }
}
