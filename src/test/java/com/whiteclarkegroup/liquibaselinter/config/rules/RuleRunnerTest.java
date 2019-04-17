package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import liquibase.change.Change;
import liquibase.change.core.RenameTableChange;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class RuleRunnerTest {

    @DisplayName("Should support ignoring specific rule types")
    @Test
    void shouldSupportSpecificRuleTypeIgnore() throws ChangeLogParseException {
        RuleRunner ruleRunner = getRuleRunner();
        ruleRunner.forChange(mockChange("Test comment lql-ignore:table-name")).checkChange();

        ChangeLogParseException changeLogParseException =
            assertThrows(ChangeLogParseException.class, () -> ruleRunner.forChange(mockChange(null)).checkChange());

        assertTrue(changeLogParseException.getMessage().contains("Table name does not follow pattern"));
    }

    private RuleRunner getRuleRunner() {
        final ListMultimap<String, RuleConfig> ruleConfigMap = ImmutableListMultimap.of("table-name", RuleConfig.builder().withEnabled(true).withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build());
        return new RuleRunner(new Config(null, ruleConfigMap, true));
    }

    private Change mockChange(String changeComment) {
        RenameTableChange change = mock(RenameTableChange.class, RETURNS_DEEP_STUBS);
        when(change.getNewTableName()).thenReturn("TBL_TABLE");
        when(change.getChangeSet().getComments()).thenReturn(changeComment);
        return change;
    }

}
