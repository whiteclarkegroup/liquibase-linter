package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.core.InsertDataChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.precondition.core.PreconditionContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NoPreconditionsRuleImplTest {
    @DisplayName("Should pass if no preconditions")
    @Test
    void shouldPassIfNoPreconditions() {
        NoPreconditionsRuleImpl rule = new NoPreconditionsRuleImpl();
        rule.configure(RuleConfig.builder().build());
        ChangeSet changeSet = new ChangeSet(mock(DatabaseChangeLog.class));
        changeSet.addChange(new InsertDataChange());
        assertFalse(rule.invalid(changeSet));
    }

    @DisplayName("Should fail on mere prescence of preconditions in changeSet")
    @Test
    void shouldFailWhenPreconditionsInChangeSet() {
        NoPreconditionsRuleImpl rule = new NoPreconditionsRuleImpl();
        rule.configure(RuleConfig.builder().build());
        ChangeSet changeSet = new ChangeSet(mock(DatabaseChangeLog.class));
        changeSet.addChange(new InsertDataChange());
        changeSet.setPreconditions(mock(PreconditionContainer.class));
        assertTrue(rule.invalid(changeSet));
    }

    @DisplayName("Should fail on mere prescence of preconditions in changeLog")
    @Test
    void shouldFailWhenPreconditionsInChangeLog() {
        NoPreconditionsRuleImpl rule = new NoPreconditionsRuleImpl();
        rule.configure(RuleConfig.builder().build());
        DatabaseChangeLog changeLog = mock(DatabaseChangeLog.class);
        when(changeLog.getPreconditions()).thenReturn(mock(PreconditionContainer.class));
        ChangeSet changeSet = new ChangeSet(changeLog);
        changeSet.addChange(new InsertDataChange());
        assertTrue(rule.invalid(changeLog));
    }
}
