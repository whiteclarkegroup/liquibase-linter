package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import com.wcg.liquibase.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({ChangeSetParameterResolver.class, RuleRunnerParameterResolver.class, DefaultConfigParameterResolver.class})
class ModifyDataChangeLinterTest {

    private ModifyDataChangeLinter modifyDataChangeLinter;

    @BeforeEach
    void setUp() {
        modifyDataChangeLinter = new ModifyDataChangeLinter();
    }

    @Test
    void shouldEnforceWhereConditionOnCertainTables(ChangeSet changeSet, Config config, RuleRunner ruleRunner) {
        for (String table : RuleType.MODIFY_DATA_ENFORCE_WHERE.create(config.getRules()).get().getRuleConfig().getRequireWhere()) {
            UpdateDataChange updateDataChange = new UpdateDataChange();
            updateDataChange.setTableName(table);
            updateDataChange.setChangeSet(changeSet);

            ChangeLogParseException changeLogParseException =
                    assertThrows(ChangeLogParseException.class, () -> modifyDataChangeLinter.lint(updateDataChange, ruleRunner));

            assertTrue(changeLogParseException.getMessage().contains("Modify data on table '" + table + "' must have a where condition"));
        }
    }

    @Test
    void shouldNotAllowWhereConditionToStartWithWhereCaseInsensitive(ChangeSet changeSet, RuleRunner ruleRunner) {
        UpdateDataChange updateDataChange = new UpdateDataChange();
        updateDataChange.setTableName("TABLE");
        updateDataChange.setChangeSet(changeSet);
        updateDataChange.setWhere("WHERE table = 'X'");
        ChangeLogParseException changeLogParseException1 =
                assertThrows(ChangeLogParseException.class, () -> modifyDataChangeLinter.lint(updateDataChange, ruleRunner));

        assertTrue(changeLogParseException1.getMessage().contains("Modify data where starts with where, that's probably a mistake"));

        updateDataChange.setTableName("TABLE");
        updateDataChange.setChangeSet(changeSet);
        updateDataChange.setWhere("where table = 'X'");
        ChangeLogParseException changeLogParseException2 =
                assertThrows(ChangeLogParseException.class, () -> modifyDataChangeLinter.lint(updateDataChange, ruleRunner));

        assertTrue(changeLogParseException2.getMessage().contains("Modify data where starts with where, that's probably a mistake"));
    }
}
