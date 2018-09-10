package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.WithFormattedErrorMessage;
import com.whiteclarkegroup.liquibaselinter.config.rules.checker.PatternChecker;
import liquibase.change.Change;

import java.util.Optional;

public class PatternRule extends Rule implements WithFormattedErrorMessage {
    private final PatternChecker patternChecker;

    public PatternRule(RuleConfig ruleConfig) {
        super(ruleConfig);
        this.patternChecker = new PatternChecker(ruleConfig);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        return patternChecker.check((String) object, change);
    }

    @Override
    public String formatErrorMessage(String errorMessage, Object object) {
        return String.format(errorMessage, Optional.ofNullable(object).orElse(""));
    }
}
