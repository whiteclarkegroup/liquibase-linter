package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class HasContextRuleImplTest {
    @DisplayName("Should pass when a context has been provided on the changeSet")
    @Test
    void shouldPassWithContexts() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getContexts().isEmpty()).thenReturn(false);
        assertFalse(new HasContextRuleImpl().invalid(changeSet));
    }

    @DisplayName("Should fail when a context has not been provided on the changeSet")
    @Test
    void shouldFailWithNoContexts() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getContexts().isEmpty()).thenReturn(true);
        assertTrue(new HasContextRuleImpl().invalid(changeSet));
    }
}
