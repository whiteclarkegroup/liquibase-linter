package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.Change;
import liquibase.change.DatabaseChange;
import liquibase.change.core.LoadDataChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IllegalChangeTypesRuleImplTest {

    @DisplayName("Null Illegal change type should be valid")
    @Test
    void nullIllegalChangeTypeShouldBeValid() {
        ChangeRule<Change> rule = new IllegalChangeTypesRuleImpl();
        rule.configure(RuleConfig.builder().build());
        assertFalse(rule.invalid(new LoadDataChange()));
    }

    @DisplayName("Empty Illegal change type should be valid")
    @Test
    void emptyIllegalChangeTypeShouldBeValid() {
        ChangeRule<Change> rule = new IllegalChangeTypesRuleImpl();
        rule.configure(RuleConfig.builder().withValues(Collections.emptyList()).build());
        assertFalse(rule.invalid(new LoadDataChange()));
    }

    @DisplayName("Mismatch Illegal change type should be valid")
    @Test
    void mismatchIllegalChangeTypeShouldBeValid() {
        ChangeRule<Change> rule = new IllegalChangeTypesRuleImpl();
        rule.configure(RuleConfig.builder().withValues(Collections.singletonList("liquibase.change.core.AddColumnChange")).build());
        assertFalse(rule.invalid(new LoadDataChange()));
    }

    @DisplayName("Illegal change type should be invalid")
    @Test
    void illegalChangeTypeShouldBeInvalid() {
        ChangeRule<Change> rule = new IllegalChangeTypesRuleImpl();
        rule.configure(RuleConfig.builder().withValues(Collections.singletonList("liquibase.change.core.LoadDataChange")).build());
        assertTrue(rule.invalid(new LoadDataChange()));
    }

    @DisplayName("Illegal change type from database change annotation name")
    @Test
    void illegalChangeTypeFromDatabaseChangeAnnotationName() {
        ChangeRule<Change> rule = new IllegalChangeTypesRuleImpl();
        rule.configure(RuleConfig.builder().withValues(Collections.singletonList(LoadDataChange.class.getAnnotation(DatabaseChange.class).name())).build());
        assertTrue(rule.invalid(new LoadDataChange()));
    }

}
