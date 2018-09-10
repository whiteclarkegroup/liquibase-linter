package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.resolvers.AddColumnChangeParameterResolver;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AddColumnChangeParameterResolver.class)
class PatternRuleTest {


    @DisplayName("Alpha pattern digits string should be invalid")
    @Test
    void alphaPatternDigitsStringShouldBeInvalid() {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().withPattern("^[A-Z]+$").build());
        assertTrue(patternRule.invalid("123", null));
    }

    @DisplayName("Alpha pattern alpha string should be valid")
    @Test
    void alphaPatternAlphaStringShouldBeValid() {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().withPattern("^[A-Z]+$").build());
        assertFalse(patternRule.invalid("ABC", null));
    }

    @DisplayName("Dynamic pattern digits string should be invalid")
    @Test
    void dyanmicPatternDigitsStringShouldBeInvalid(AddColumnChange columnChange) {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().withPattern("^TEST-{{value}}$").withDynamicValue("tableName").build());
        columnChange.setTableName("MAGIC");
        assertTrue(patternRule.invalid("123", columnChange));
    }

    @DisplayName("Dynamic pattern value should be valid")
    @Test
    void dyanmicPatternDigitsVaueShouldBeInvalid(AddColumnChange columnChange) {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().withPattern("^TEST-{{value}}$").withDynamicValue("tableName").build());
        columnChange.setTableName("MAGIC");
        assertFalse(patternRule.invalid("TEST-MAGIC", columnChange));
    }

    @DisplayName("Null pattern should be valid")
    @Test
    void nullShouldBeValid() {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().build());
        assertFalse(patternRule.invalid("123", null));
    }

    @DisplayName("Null value should be valid")
    @Test
    void nullValueShouldBeValid() {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().withPattern("^.+$").build());
        assertFalse(patternRule.invalid(null, null));
    }

}
