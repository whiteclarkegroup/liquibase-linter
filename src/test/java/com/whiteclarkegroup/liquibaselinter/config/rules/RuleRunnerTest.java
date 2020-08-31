package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import liquibase.change.Change;
import liquibase.change.core.RenameTableChange;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.IGNORED;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.PASSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class RuleRunnerTest {
    @DisplayName("Should add rule violation to report as an error")
    @Test
    void shouldReportErrorsForFailureWhenNotIgnored() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false);

        ruleRunner.checkChange(mockInvalidChange(null, "TBL_TABLE"));

        Report report = ruleRunner.buildReport();
        assertThat(report.getItems()).extracting("type").containsExactly(ERROR, PASSED);
        assertThat(report.getItems()).extracting("message").contains("Table name does not follow pattern");
    }

    @DisplayName("Should add rule violations to report as errors from additional configs of same rule")
    @Test
    void shouldReportErrorsForMultipleRuleConfigFailures() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false);

        ruleRunner.checkChange(mockInvalidChange(null, "FOO_TABLE"));

        Report report = ruleRunner.buildReport();
        assertThat(report.getItems()).extracting("type").containsExactly(PASSED, ERROR);
        assertThat(report.getItems()).extracting("message").contains("Table name does not follow pattern");
    }

    @DisplayName("Should add rule violation to report as ignored, when an ignore comment matches")
    @Test
    void shouldReportIgnoredWhenIgnored() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, false);

        ruleRunner.checkChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE"));

        Report report = ruleRunner.buildReport();
        assertThat(report.getItems()).extracting("type").containsExactly(IGNORED, PASSED);
        assertThat(report.getItems()).extracting("message").contains("Table name does not follow pattern");
    }

    @DisplayName("Should throw exception for rule violation when fail-fast is on")
    @Test
    void shouldThrowForErrorsWhenFailFastOn() {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, true);

        assertThatExceptionOfType(ChangeLogParseException.class).isThrownBy(() -> {
            ruleRunner.checkChange(mockInvalidChange(null, "TBL_TABLE"));
        }).withMessageContaining("Table name does not follow pattern");
    }

    @DisplayName("Should report rule violation as ignored when ignored via comment and fail-fast is on")
    @Test
    void shouldNotThrowForIgnoredWhenFailFastOn() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule(null, true);

        ruleRunner.checkChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE"));

        Report report = ruleRunner.buildReport();
        assertThat(report.getItems()).extracting("type").containsExactly(IGNORED, PASSED);
        assertThat(report.getItems()).extracting("message").contains("Table name does not follow pattern");
    }

    @DisplayName("Should add rule violation to report as an error when condition resolves to true")
    @Test
    void shouldReportErrorWhenConditionTrue() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("true", false);

        ruleRunner.checkChange(mockInvalidChange(null, "TBL_TABLE"));

        Report report = ruleRunner.buildReport();
        assertThat(report.getItems()).extracting("type").containsExactly(ERROR, PASSED);
        assertThat(report.getItems()).extracting("message").contains("Table name does not follow pattern");
    }

    @DisplayName("Should add rule violation to report as ignored when condition resolves to false")
    @Test
    void shouldReportIgnoredWhenConditionTrue() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("true", false);

        ruleRunner.checkChange(mockInvalidChange("Test comment lql-ignore:table-name", "TBL_TABLE"));

        Report report = ruleRunner.buildReport();
        assertThat(report.getItems()).extracting("type").containsExactly(IGNORED, PASSED);
        assertThat(report.getItems()).extracting("message").contains("Table name does not follow pattern");
    }

    @DisplayName("Should not report violation when condition resolves to false")
    @Test
    void shouldNotReportWhenConditionFalse() throws ChangeLogParseException {
        RuleRunner ruleRunner = ruleRunnerWithTableNameRule("false", false);

        ruleRunner.checkChange(mockInvalidChange(null, "TBL_TABLE"));

        Report report = ruleRunner.buildReport();
        assertThat(report.getItems()).isEmpty();
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
        return new RuleRunner(new Config.Builder().withRules(ruleConfigMap).withFailFast(failFast).withEnableAfter(enableAfter).build(),
            new ArrayList<>(), new HashSet<>());
    }

    private Change mockInvalidChange(String changeComment, String tableName) {
        RenameTableChange change = mock(RenameTableChange.class, RETURNS_DEEP_STUBS);
        when(change.getNewTableName()).thenReturn(tableName);
        when(change.getChangeSet().getComments()).thenReturn(changeComment);
        return change;
    }

}
