package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.DropNotNullConstraintChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class})
class DropNotNullConstraintChangeLinterTest {

    private DropNotNullConstraintChangeLinter dropNotNullConstraintChangeLinter;

    @BeforeEach
    void setUp() {
        dropNotNullConstraintChangeLinter = new DropNotNullConstraintChangeLinter();
    }

    @DisplayName("Should allow non null column data type")
    @Test
    void shouldAllowNonNullColumnDataType(ChangeSet changeSet, RuleRunner ruleRunner) {
        try {
            dropNotNullConstraintChangeLinter.lint(build(changeSet, "NVARCHAR(10)"), ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @DisplayName("Should not allow null column data type")
    @Test
    void shouldNotAllowNullColumnDataType(ChangeSet changeSet, RuleRunner ruleRunner) {
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> dropNotNullConstraintChangeLinter.lint(build(changeSet, null), ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Drop not null constraint column data type attribute must be populated"));
    }

    @DisplayName("Should not allow blank column data type")
    @Test
    void shouldNotAllowBlankColumnDataType(ChangeSet changeSet, RuleRunner ruleRunner) {
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
