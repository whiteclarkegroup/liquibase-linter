package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.DatabaseChange;
import liquibase.change.core.LoadDataChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IllegalChangeTypesTest {

    @DisplayName("Null Illegal change type should be valid")
    @Test
    void nullIllegalChangeTypeShouldBeValid() {
        IllegalChangeTypes illegalChangeTypes = new IllegalChangeTypes(RuleConfig.builder().build());
        assertFalse(illegalChangeTypes.invalid(new LoadDataChange(), null));
    }

    @DisplayName("Empty Illegal change type should be valid")
    @Test
    void emptyIllegalChangeTypeShouldBeValid() {
        IllegalChangeTypes illegalChangeTypes = new IllegalChangeTypes(RuleConfig.builder().withValues(Collections.emptyList()).build());
        assertFalse(illegalChangeTypes.invalid(new LoadDataChange(), null));
    }

    @DisplayName("Mismatch Illegal change type should be valid")
    @Test
    void mismatchIllegalChangeTypeShouldBeValid() {
        IllegalChangeTypes illegalChangeTypes = new IllegalChangeTypes(RuleConfig.builder().withValues(Collections.singletonList("liquibase.change.core.AddColumnChange")).build());
        assertFalse(illegalChangeTypes.invalid(new LoadDataChange(), null));
    }

    @DisplayName("Illegal change type should be invalid")
    @Test
    void illegalChangeTypeShouldBeInvalid() {
        IllegalChangeTypes illegalChangeTypes = new IllegalChangeTypes(RuleConfig.builder().withValues(Collections.singletonList("liquibase.change.core.LoadDataChange")).build());
        assertTrue(illegalChangeTypes.invalid(new LoadDataChange(), null));
    }

    @DisplayName("Illegal change type from database change annotation name")
    @Test
    void illegalChangeTypeFromDatabaseChangeAnnotationName() {
        IllegalChangeTypes illegalChangeTypes = new IllegalChangeTypes(RuleConfig.builder().withValues(Collections.singletonList(LoadDataChange.class.getAnnotation(DatabaseChange.class).name())).build());
        assertTrue(illegalChangeTypes.invalid(new LoadDataChange(), null));
    }

}
