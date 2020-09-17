package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.report.ReportItem;
import liquibase.change.Change;
import liquibase.change.core.RenameTableChange;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class RuleRunnerTest {
    @DisplayName("Should add rule violation to report as an error")
    @Test
    void shouldReportErrorsForFailureWhenNotIgnored() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false);

        ruleRunner.checkChange(mockInvalidChange(null, "TBL_TABLE"));

        assertEquals(0, ruleRunner.getReport().countIgnored());
        assertEquals(1, ruleRunner.getReport().countErrors());
        assertEquals(0, ruleRunner.getReport().countSkipped());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violations to report as errors from additional configs of same rule")
    @Test
    void shouldReportErrorsForMultipleRuleConfigFailures() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false);

        ruleRunner.checkChange(mockInvalidChange(null, "FOO_TABLE"));

        assertEquals(0, ruleRunner.getReport().countIgnored());
        assertEquals(1, ruleRunner.getReport().countErrors());
        assertEquals(0, ruleRunner.getReport().countSkipped());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violation to report as ignored, when an ignore comment matches")
    @Test
    void shouldReportIgnoredWhenIgnored() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false);

        ruleRunner.checkChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE"));

        assertEquals(0, ruleRunner.getReport().countErrors());
        assertEquals(1, ruleRunner.getReport().countIgnored());
        assertEquals(0, ruleRunner.getReport().countSkipped());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getItems().iterator().next().getMessage());
    }

    @DisplayName("Should throw exception for rule violation when fail-fast is on")
    @Test
    void shouldThrowForErrorsWhenFailFastOn() {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, true);

        ChangeLogParseException changeLogParseException =
            assertThrows(ChangeLogParseException.class, () -> ruleRunner.checkChange(mockInvalidChange(null, "TBL_TABLE")));

        assertTrue(changeLogParseException.getMessage().contains("Table name does not follow pattern"));
    }

    @DisplayName("Should report rule violation as ignored when ignored via comment and fail-fast is on")
    @Test
    void shouldNotThrowForIgnoredWhenFailFastOn() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, true);

        ruleRunner.checkChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE"));

        assertEquals(0, ruleRunner.getReport().countErrors());
        assertEquals(1, ruleRunner.getReport().countIgnored());
        assertEquals(0, ruleRunner.getReport().countSkipped());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violation to report as an error when condition resolves to true")
    @Test
    void shouldReportErrorWhenConditionTrue() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("true", false);

        ruleRunner.checkChange(mockInvalidChange(null, "TBL_TABLE"));

        assertEquals(0, ruleRunner.getReport().countIgnored());
        assertEquals(1, ruleRunner.getReport().countErrors());
        assertEquals(0, ruleRunner.getReport().countSkipped());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violation to report as ignored when condition resolves to false")
    @Test
    void shouldReportIgnoredWhenConditionTrue() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("true", false);

        ruleRunner.checkChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE"));

        assertEquals(0, ruleRunner.getReport().countErrors());
        assertEquals(1, ruleRunner.getReport().countIgnored());
        assertEquals(0, ruleRunner.getReport().countSkipped());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violation to report as skipped when enable-after condition not met")
    @Test
    void shouldReportSkippedBeforeEnabledAfter() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false, "enabledAfter.xml");

        ruleRunner.checkChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE"));

        assertEquals(0, ruleRunner.getReport().countErrors());
        assertEquals(0, ruleRunner.getReport().countIgnored());
        assertEquals(1, ruleRunner.getReport().countSkipped());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getItems().iterator().next().getMessage());
    }

    @DisplayName("Should add rule violation to report as ignored when enable-after condition is met")
    @Test
    void shouldReportIgnoredAfterEnabledAfter() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false, "enabledAfter.xml");

        ruleRunner.getFilesParsed().add("enabledAfter.xml");
        ruleRunner.checkChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE"));

        assertEquals(0, ruleRunner.getReport().countErrors());
        assertEquals(1, ruleRunner.getReport().countIgnored());
        assertEquals(0, ruleRunner.getReport().countSkipped());
        assertEquals("Table name does not follow pattern", ruleRunner.getReport().getItems().iterator().next().getMessage());
    }

    @DisplayName("Should not report violation when condition resolves to false")
    @Test
    void shouldNotReportErrorWhenConditionFalse() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("false", false);

        ruleRunner.checkChange(mockInvalidChange(null, "TBL_TABLE"));

        assertEquals(ruleRunner.getReport().countErrors(), 0);
        assertEquals(ruleRunner.getReport().countIgnored(), 0);
        assertEquals(ruleRunner.getReport().countSkipped(), 0);
    }

    @DisplayName("Should not report ignored violation when condition resolves to false")
    @Test
    void shouldNotReportIgnoredWhenConditionFalse() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("false", false);

        ruleRunner.checkChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE"));

        assertEquals(ruleRunner.getReport().countErrors(), 0);
        assertEquals(ruleRunner.getReport().countIgnored(), 0);
        assertEquals(ruleRunner.getReport().countSkipped(), 0);
    }

    private RuleRunner ruleRunnerWithTableNameRule(String condition, boolean failFast) {
        return ruleRunnerWithTableNameRule(condition, failFast, null);
    }

    private RuleRunner ruleRunnerWithTableNameRule(String condition, boolean failFast, String enableAfter) {
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
        return new RuleRunner(new Config.Builder().withRules(ruleConfigMap).withFailFast(failFast).withEnableAfter(enableAfter).build(), new HashSet<>());
    }

    private Change mockInvalidChange(String changeComment, String tableName) {
        RenameTableChange change = mock(RenameTableChange.class, RETURNS_DEEP_STUBS);
        when(change.getNewTableName()).thenReturn(tableName);
        when(change.getChangeSet().getComments()).thenReturn(changeComment);
        return change;
    }

}
