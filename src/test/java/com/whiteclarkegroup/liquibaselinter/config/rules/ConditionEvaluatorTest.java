package com.whiteclarkegroup.liquibaselinter.config.rules;

import liquibase.ContextExpression;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ConditionEvaluatorTest {

    @Test
    void should_match_simple_context() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("foo"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();
        assertTrue(ConditionEvaluator.evaluateCondition(ruleConfig, changeSet));
    }

    @Test
    void should_not_match_negative_simple_context() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("!foo"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();
        assertFalse(ConditionEvaluator.evaluateCondition(ruleConfig, changeSet));
    }

    @Test
    void should_not_match_simple_context_mismatch() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("foo"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();
        assertFalse(ConditionEvaluator.evaluateCondition(ruleConfig, changeSet));
    }

    @Test
    void should_not_match_no_context() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();
        assertFalse(ConditionEvaluator.evaluateCondition(ruleConfig, changeSet));
    }

    @Test
    void should_match_multiple_and_contexts() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("foo and bar"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo', 'bar')").build();
        assertTrue(ConditionEvaluator.evaluateCondition(ruleConfig, changeSet));
    }

}
