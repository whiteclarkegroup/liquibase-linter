package com.wcg.liquibase;

import com.google.common.collect.ImmutableSet;
import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import com.wcg.liquibase.resolvers.RuleRunnerParameterResolver;
import liquibase.ContextExpression;
import liquibase.change.Change;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.InsertDataChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.precondition.core.PreconditionContainer;
import liquibase.precondition.core.SqlPrecondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, DefaultConfigParameterResolver.class, RuleRunnerParameterResolver.class})
class ChangeLogLinterTest {

    private ChangeLogLinter changeLogLinter;

    @BeforeEach
    void setUp() {
        changeLogLinter = new ChangeLogLinter();
    }

    @DisplayName("Change set should have at least one context")
    @Test
    void should_have_at_least_one_context_per_change_set(ChangeSet changeSet, Config config, RuleRunner ruleRunner) {
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> changeLogLinter.lintChangeLog(changeSet.getChangeLog(), config, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Should have at least one context on the change set"));
    }

    @DisplayName("Should lint change sets with standard comment")
    @Test
    void should_lint_change_sets_with_standard_comment(Config config, RuleRunner ruleRunner) throws ChangeLogParseException {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("ddl_test"), "Test Data column");
        changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner);
        verify(changeSet, times(1)).getChanges();
    }


    @DisplayName("Should not lint change sets with lint disabled comment")
    @Test
    void should_not_lint_change_sets_with_lint_disabled_comment(Config config, RuleRunner ruleRunner) throws ChangeLogParseException {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, null, "coment includes lql-ignore foo");
        changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner);
        verify(changeSet, never()).getChanges();
    }

    @DisplayName("Should not fall over on null comment")
    @Test
    void should_not_fall_over_on_null_comment(Config config, RuleRunner ruleRunner) throws ChangeLogParseException {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("ddl_test"), "Comment");
        changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner);
        verify(changeSet, times(1)).getChanges();
    }

    @DisplayName("Should not allow more than one ddl_test change in a change set")
    @Test
    void should_not_allow_more_than_one_ddl_test_change_in_a_change_set(Config config, RuleRunner ruleRunner) {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("ddl_test"), "Comment");
        addChangeToChangeSet(changeSet, new AddColumnChange(), new AddColumnChange());

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Should only have a single ddl change per change set"));
    }

    @DisplayName("Should allow one ddl_test change in a change set")
    @Test
    void should_allow_one_ddl_test_change_in_a_change_set(Config config, RuleRunner ruleRunner) throws ChangeLogParseException {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("ddl_test"), "Comment");
        addChangeToChangeSet(changeSet, new AddColumnChange());

        changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner);
    }

    @DisplayName("Should not allow ddl_test changes in context other than ddl_test")
    @Test
    void should_not_allow_ddl_test_changes_in_context_other_than_ddl_test(Config config, RuleRunner ruleRunner) {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("dml_test"), "Comment");
        addChangeToChangeSet(changeSet, new AddColumnChange());

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Should have a ddl changes under ddl contexts"));
    }

    @DisplayName("Should not allow dml changes in ddl_test context")
    @Test
    void should_not_allow_dml_changes_in_ddl_test_context(Config config, RuleRunner ruleRunner) {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("ddl_test"), "Comment");
        addChangeToChangeSet(changeSet, new InsertDataChange());

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Should have a ddl changes under ddl contexts"));
    }

    @DisplayName("Should not allow spaces in filename - it causes issues on some platforms")
    @Test
    void should_not_allow_spaces_in_filename(Config config, RuleRunner ruleRunner) {
        DatabaseChangeLog passingChangelog = mock(DatabaseChangeLog.class);
        when(passingChangelog.getFilePath()).thenReturn("modules/foo/nice.xml");

        DatabaseChangeLog failingChangelog = mock(DatabaseChangeLog.class);
        when(failingChangelog.getFilePath()).thenReturn("modules/foo/whoops space.xml");

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> changeLogLinter.lintChangeLog(failingChangelog, config, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Changelog filenames should not contain spaces"));

    }

    @DisplayName("Should not lint baseline script")
    @Test
    void should_not_lint_baseline_script(Config config, RuleRunner ruleRunner) throws ChangeLogParseException {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("baseline_ddl_test"), "Test Data column");
        changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner);
        verify(changeSet, never()).getChanges();
    }

    @DisplayName("Should not allow change set without a comment")
    @Test
    void should_not_allow_change_log_without_comment(Config config, RuleRunner ruleRunner) {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("dml"), null);
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Change set must have a comment"));
    }

    @DisplayName("Should not context with suffix not ending in _test  or _script")
    @Test
    void should_not_allow_context_with_suffix_not_ending_in_allowed(Config config, RuleRunner ruleRunner) {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("dml"), "Comment");
        addChangeToChangeSet(changeSet, new AddColumnChange());

        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Context is incorrect, should end with '_test' or '_script'"));
    }

    @Test
    void should_prevent_precondition(Config config, RuleRunner ruleRunner) {
        DatabaseChangeLog databaseChangeLog = mock(DatabaseChangeLog.class);
        ChangeSet changeSet = getChangeSet(databaseChangeLog, ImmutableSet.of("core_test"), "Comment");
        SqlPrecondition precondition = new SqlPrecondition();
        precondition.setSql("SELECT COUNT(*) FROM BAR");
        precondition.setExpectedResult("0");
        PreconditionContainer preconditionContainer = new PreconditionContainer();
        preconditionContainer.setOnFail("MARK_RAN");
        preconditionContainer.addNestedPrecondition(precondition);
        when(changeSet.getPreconditions()).thenReturn(preconditionContainer);
        addChangeToChangeSet(changeSet, new InsertDataChange());

        ChangeLogParseException changeLogParseException = assertThrows(ChangeLogParseException.class, () -> changeLogLinter.lintChangeLog(databaseChangeLog, config, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Preconditions are not allowed in this project"));
    }

    private <T extends Change> void addChangeToChangeSet(ChangeSet changeSet, T... changes) {
        when(changeSet.getChanges()).thenReturn(Arrays.asList(changes));
        for (T change : changes) {
            change.setChangeSet(changeSet);
        }
    }

    private ChangeSet getChangeSet(DatabaseChangeLog databaseChangeLog, Set<String> contexts, String comment) {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getComments()).thenReturn(comment);
        if (contexts != null) {
            ContextExpression expression = mock(ContextExpression.class);
            when(changeSet.getContexts()).thenReturn(expression);
            when(expression.getContexts()).thenReturn(contexts);
        }
        when(databaseChangeLog.getChangeSets()).thenReturn(Collections.singletonList(changeSet));
        return changeSet;
    }

}
