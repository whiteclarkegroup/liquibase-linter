package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.resolvers.AddColumnChangeParameterResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AddColumnChangeParameterResolver.class)
class MandatoryPatternRuleTest {

    @DisplayName("Null value should be invalid")
    @Test
    void nullValueShouldBeInvalid() {
        MandatoryPatternRule patternRule = new MandatoryPatternRule(RuleConfig.builder().withPattern("^.+$").build());
        assertTrue(patternRule.invalid(null, null));
    }

    @DisplayName("Empty string value should be invalid")
    @Test
    void emptyValueShouldBeInvalid() {
        MandatoryPatternRule patternRule = new MandatoryPatternRule(RuleConfig.builder().withPattern("^.+$").build());
        assertTrue(patternRule.invalid("", null));
    }

}
