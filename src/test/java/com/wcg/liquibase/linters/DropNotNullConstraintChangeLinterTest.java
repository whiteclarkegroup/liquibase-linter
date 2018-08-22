package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.DropNotNullConstraintChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
class DropNotNullConstraintChangeLinterTest {

    private DropNotNullConstraintChangeLinter dropNotNullConstraintChangeLinter;

    @BeforeEach
    void setUp() {
        dropNotNullConstraintChangeLinter = new DropNotNullConstraintChangeLinter();
    }

    @DisplayName("Should allow non null column data type")
    @Test
    void should_allow_non_null_column_data_type(ChangeSet changeSet, RuleRunner ruleRunner) throws ChangeLogParseException {
        dropNotNullConstraintChangeLinter.lint(build(changeSet, "NVARCHAR(10)"), ruleRunner);
    }

    @DisplayName("Should not allow null column data type")
    @Test
    void should_not_allow_null_column_data_type(ChangeSet changeSet, RuleRunner ruleRunner) {
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> dropNotNullConstraintChangeLinter.lint(build(changeSet, null), ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Drop not null constraint column data type attribute must be populated"));
    }

    @DisplayName("Should not allow blank column data type")
    @Test
    void should_not_allow_blank_column_data_type(ChangeSet changeSet, RuleRunner ruleRunner) {
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> dropNotNullConstraintChangeLinter.lint(build(changeSet, ""), ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Drop not null constraint column data type attribute must be populated"));
    }

    private DropNotNullConstraintChange build(ChangeSet changeSet, String columnDataType) {
        DropNotNullConstraintChange change = new DropNotNullConstraintChange();
        change.setColumnDataType(columnDataType);
        change.setChangeSet(changeSet);
        return change;
    }

}
