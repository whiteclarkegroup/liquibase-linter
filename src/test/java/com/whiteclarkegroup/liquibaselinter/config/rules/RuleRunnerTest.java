package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import liquibase.change.Change;
import liquibase.change.core.RenameTableChange;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class RuleRunnerTest {
    @DisplayName("Should add rule violation to report as an error")
    @Test
    void shouldReportErrorsForFailureWhenNotIgnored() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false);

        ruleRunner.forChange(mockInvalidChange(null, "TBL_TABLE")).checkChange();

        assertEquals(0, ruleRunner.getReport().countIgnored());
        assertEquals(1, ruleRunner.getReport().countErrors());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getReportItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violations to report as errors from additional configs of same rule")
    @Test
    void shouldReportErrorsForMultipleRuleConfigFailures() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false);

        ruleRunner.forChange(mockInvalidChange(null, "FOO_TABLE")).checkChange();

        assertEquals(0, ruleRunner.getReport().countIgnored());
        assertEquals(1, ruleRunner.getReport().countErrors());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getReportItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violation to report as ignored, when an ignore comment matches")
    @Test
    void shouldReportIgnoredWhenIgnored() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false);

        ruleRunner.forChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE")).checkChange();

        assertEquals(0, ruleRunner.getReport().countErrors());
        assertEquals(1, ruleRunner.getReport().countIgnored());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getReportItems().iterator().next().getMessage());
    }

    @DisplayName("Should throw exception for rule violation when fail-fast is on")
    @Test
    void shouldThrowForErrorsWhenFailFastOn() {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, true);

        ChangeLogParseException changeLogParseException =
            assertThrows(ChangeLogParseException.class, () -> ruleRunner.forChange(mockInvalidChange(null, "TBL_TABLE")).checkChange());

        assertTrue(changeLogParseException.getMessage().contains("Table name does not follow pattern"));
    }

    @DisplayName("Should report rule violation as ignored when ignored via comment and fail-fast is on")
    @Test
    void shouldNotThrowForIgnoredWhenFailFastOn() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, true);

        ruleRunner.forChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE")).checkChange();

        assertEquals(0, ruleRunner.getReport().countErrors());
        assertEquals(1, ruleRunner.getReport().countIgnored());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getReportItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violation to report as an error when condition resolves to true")
    @Test
    void shouldReportErrorWhenConditionTrue() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("true", false);

        ruleRunner.forChange(mockInvalidChange(null, "TBL_TABLE")).checkChange();

        assertEquals(0, ruleRunner.getReport().countIgnored());
        assertEquals(1, ruleRunner.getReport().countErrors());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getReportItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violation to report as ignored when condition resolves to false")
    @Test
    void shouldReportIgnoredWhenConditionTrue() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("true", false);

        ruleRunner.forChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE")).checkChange();

        assertEquals(0, ruleRunner.getReport().countErrors());
        assertEquals(1, ruleRunner.getReport().countIgnored());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getReportItems().iterator().next().getMessage());
    }

    @DisplayName("Should not report violation when condition resolves to false")
    @Test
    void shouldNotReportErrorWhenConditionFalse() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("false", false);

        ruleRunner.forChange(mockInvalidChange(null, "TBL_TABLE")).checkChange();

        assertFalse(ruleRunner.getReport().hasItems());
    }

    @DisplayName("Should not report ignored violation when condition resolves to false")
    @Test
    void shouldNotReportIgnoredWhenConditionFalse() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("false", false);

        ruleRunner.forChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE")).checkChange();

        assertFalse(ruleRunner.getReport().hasItems());
    }

    private RuleRunner ruleRunnerWithTableNameRule(String condition, boolean failFast) {
        final ListMultimap<String, RuleConfig> ruleConfigMap = ImmutableListMultimap.of(
            "table-name", RuleConfig.builder()
                .withEnabled(true)
                .withPattern("^(?!TBL)[A-Z_]+(?<!_)$")
                .withCondition(condition)
                .build(),
            "table-name", RuleConfig.builder()
                .withEnabled(true)
                .withPattern("^(?!FOO)[A-Z_]+(?<!_)$")
                .withCondition(condition)
                .build());
        return new RuleRunner(new Config(null, null, ruleConfigMap, failFast, null), new HashSet<>());
    }

    private Change mockInvalidChange(String changeComment, String tableName) {
        RenameTableChange change = mock(RenameTableChange.class, RETURNS_DEEP_STUBS);
        when(change.getNewTableName()).thenReturn(tableName);
        when(change.getChangeSet().getComments()).thenReturn(changeComment);
        return change;
    }

}
