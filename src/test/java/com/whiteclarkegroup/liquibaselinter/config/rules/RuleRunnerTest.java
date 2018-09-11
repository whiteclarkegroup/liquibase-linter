package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.google.common.collect.ImmutableMap;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import liquibase.change.Change;
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
        ruleRunner.forChange(mockChange("Test comment lql-ignore:schema-name,table-name")).run(RuleType.TABLE_NAME, "TBL_TEST");

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> ruleRunner.forChange(mockChange(null)).run(RuleType.TABLE_NAME, "TBL_TEST"));

        assertTrue(changeLogParseException.getMessage().contains("Table name does not follow pattern"));
    }

    private RuleRunner getRuleRunner() {
        final ImmutableMap<String, RuleConfig> ruleConfigMap = ImmutableMap.of(RuleType.TABLE_NAME.getKey(), RuleConfig.builder().withEnabled(true).withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build());
        return new RuleRunner(new Config(null, ruleConfigMap, true));
    }

    private Change mockChange(String changeComment) {
        Change change = mock(Change.class, RETURNS_DEEP_STUBS);
        when(change.getChangeSet().getComments()).thenReturn(changeComment);
        return change;
    }

}
