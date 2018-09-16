package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.Change;
import liquibase.change.core.InsertDataChange;
import liquibase.change.core.LoadDataChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.precondition.core.PreconditionContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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

    @DisplayName("Should fail on mere prescence of preconditions")
    @Test
    void shouldFailWhenPreconditions() {
        NoPreconditionsRuleImpl rule = new NoPreconditionsRuleImpl();
        rule.configure(RuleConfig.builder().build());
        ChangeSet changeSet = new ChangeSet(mock(DatabaseChangeLog.class));
        changeSet.addChange(new InsertDataChange());
        changeSet.setPreconditions(mock(PreconditionContainer.class));
        assertTrue(rule.invalid(changeSet));
    }
}
