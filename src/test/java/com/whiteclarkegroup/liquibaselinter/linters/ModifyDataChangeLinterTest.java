package com.whiteclarkegroup.liquibaselinter.linters;

import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.DefaultConfigParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
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
        for (String table : RuleType.MODIFY_DATA_ENFORCE_WHERE.create(config.getRules()).get().getRuleConfig().getValues()) {
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
