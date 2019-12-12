package com.whiteclarkegroup.liquibaselinter.config.rules;

import liquibase.ContextExpression;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ConditionHelperTest {

    @Test
    void shouldMatchSimpleContext() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("foo"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();
        assertTrue(ConditionHelper.evaluateCondition(ruleConfig, changeSet));
    }

    @Test
    void shouldNotMatchNegativeSimpleContext() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("!foo"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();
        assertFalse(ConditionHelper.evaluateCondition(ruleConfig, changeSet));
    }

    @Test
    void shouldNotNatchSimpleContextMismatch() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("foo"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();
        assertFalse(ConditionHelper.evaluateCondition(ruleConfig, changeSet));
    }

    @Test
    void shouldNotMatchNoContext() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();
        assertFalse(ConditionHelper.evaluateCondition(ruleConfig, changeSet));
    }

    @Test
    void shouldMatchMultipleAndContexts() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("foo and bar"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo', 'bar')").build();
        assertTrue(ConditionHelper.evaluateCondition(ruleConfig, changeSet));
    }

}
