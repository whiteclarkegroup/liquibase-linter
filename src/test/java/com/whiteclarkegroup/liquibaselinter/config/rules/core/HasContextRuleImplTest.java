package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class HasContextRuleImplTest {
    @Test
    void should_pass_if_contexts() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getContexts().isEmpty()).thenReturn(false);
        assertFalse(new HasContextRuleImpl().invalid(changeSet));
    }

    @Test
    void should_fail_if_no_contexts() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getContexts().isEmpty()).thenReturn(true);
        assertTrue(new HasContextRuleImpl().invalid(changeSet));
    }
}
