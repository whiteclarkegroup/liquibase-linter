package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.AddColumnChangeParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.change.AddColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AddColumnChangeParameterResolver.class, RuleRunnerParameterResolver.class})
class ColumnConfigLinterTest {

    private ColumnConfigLinter columnConfigLinter;

    @BeforeEach
    void setUp() {
        columnConfigLinter = new ColumnConfigLinter();
    }

    @DisplayName("Should not allow add column without remarks")
    @Test
    void shouldNotAllowAddColumnWithoutRemarks(AddColumnChange addColumnChange, RuleRunner ruleRunner) {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnChange.addColumn(addColumnConfig);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> columnConfigLinter.lintColumnConfig(addColumnChange, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Add column must contain remarks"));

    }

    @DisplayName("Should allow add column with remarks")
    @Test
    void shouldAllowAddColumnWithRemarksAndNullable(AddColumnChange addColumnChange, RuleRunner ruleRunner) {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnConfig.setRemarks("REMARK");
        final ConstraintsConfig constraints = new ConstraintsConfig();
        constraints.setNullable(true);
        addColumnConfig.setConstraints(constraints);
        addColumnChange.addColumn(addColumnConfig);

        try {
            columnConfigLinter.lintColumnConfig(addColumnChange, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @DisplayName("Should enforce use of nullable constraint")
    @Test
    void shouldEnforceUseOfNullableConstraint(AddColumnChange addColumnChange, RuleRunner ruleRunner) {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnConfig.setRemarks("REMARK");
        final ConstraintsConfig constraints = new ConstraintsConfig();
        addColumnConfig.setConstraints(constraints);
        addColumnChange.addColumn(addColumnConfig);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> columnConfigLinter.lintColumnConfig(addColumnChange, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Add column must specify nullable constraint"));
    }

    @DisplayName("Should not allow primary key attribute")
    @Test
    void shouldNotAllowPrimaryKeyAttribute(AddColumnChange addColumnChange, RuleRunner ruleRunner) {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnConfig.setRemarks("REMARK");
        final ConstraintsConfig constraints = new ConstraintsConfig();
        constraints.setNullable(true);
        constraints.setPrimaryKey(true);
        addColumnConfig.setConstraints(constraints);
        addColumnChange.addColumn(addColumnConfig);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> columnConfigLinter.lintColumnConfig(addColumnChange, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Add column must not use primary key attribute. Instead use AddPrimaryKey change type"));
    }
}
