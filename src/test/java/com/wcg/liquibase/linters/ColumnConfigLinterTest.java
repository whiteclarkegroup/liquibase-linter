package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.AddColumnChangeParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.change.AddColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({AddColumnChangeParameterResolver.class, DefaultConfigParameterResolver.class})
class ColumnConfigLinterTest {

    private ColumnConfigLinter columnConfigLinter;

    @BeforeEach
    void setUp() {
        columnConfigLinter = new ColumnConfigLinter();
    }

    @DisplayName("Should not allow add column without remarks")
    @Test
    void should_not_allow_add_column_without_remarks(AddColumnChange addColumnChange, Config config) {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnChange.addColumn(addColumnConfig);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> columnConfigLinter.lintColumnConfig(addColumnChange, config.getRules()));

        assertTrue(changeLogParseException.getMessage().contains("Add column must contain remarks"));

    }

    @DisplayName("Should allow add column with remarks")
    @Test
    void should_allow_add_column_with_remarks_and_nullable(AddColumnChange addColumnChange, Config config) throws ChangeLogParseException {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnConfig.setRemarks("REMARK");
        final ConstraintsConfig constraints = new ConstraintsConfig();
        constraints.setNullable(true);
        addColumnConfig.setConstraints(constraints);
        addColumnChange.addColumn(addColumnConfig);

        columnConfigLinter.lintColumnConfig(addColumnChange, config.getRules());
    }

    @DisplayName("Should enforce use of nullable constraint")
    @Test
    void should_enforce_use_of_nullable_constraint(AddColumnChange addColumnChange, Config config) {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnConfig.setRemarks("REMARK");
        final ConstraintsConfig constraints = new ConstraintsConfig();
        addColumnConfig.setConstraints(constraints);
        addColumnChange.addColumn(addColumnConfig);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> columnConfigLinter.lintColumnConfig(addColumnChange, config.getRules()));

        assertTrue(changeLogParseException.getMessage().contains("Add column must specify nullable constraint"));
    }

    @DisplayName("Should not allow primary key attribute")
    @Test
    void should_not_allow_primary_key_attribute(AddColumnChange addColumnChange, Config config) {
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName("TEST");
        addColumnConfig.setRemarks("REMARK");
        final ConstraintsConfig constraints = new ConstraintsConfig();
        constraints.setNullable(true);
        constraints.setPrimaryKey(true);
        addColumnConfig.setConstraints(constraints);
        addColumnChange.addColumn(addColumnConfig);

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> columnConfigLinter.lintColumnConfig(addColumnChange, config.getRules()));

        assertTrue(changeLogParseException.getMessage().contains("Add column must not use primary key attribute. Instead use AddPrimaryKey change type"));
    }
}
