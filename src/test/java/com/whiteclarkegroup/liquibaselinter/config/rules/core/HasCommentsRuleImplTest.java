package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class HasCommentsRuleImplTest {

    @DisplayName("Should pass when a comment has been provided on the changeSet")
    @Test
    void shouldPassWithPopulatedComment() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn("Some comment");
        assertFalse(new HasCommentRuleImpl().invalid(changeSet));
    }

    @DisplayName("Should fail when a comment has not been provided on the changeSet")
    @Test
    void shouldFailWithNoComment() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn(null);
        assertTrue(new HasCommentRuleImpl().invalid(changeSet));
    }

    @DisplayName("Should fail when a comment is blank on the changeSet")
    @Test
    void shouldFailWithBlankComment() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn("");
        assertTrue(new HasCommentRuleImpl().invalid(changeSet));
    }

}
