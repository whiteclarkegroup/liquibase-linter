package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import com.whiteclarkegroup.liquibaselinter.ChangeLogLinter;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.generic.PatternRule;
import liquibase.change.Change;

import java.util.Collection;

public class SeparateDDLContexts extends PatternRule {

    public SeparateDDLContexts(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        Collection<String> contexts = (Collection<String>) object;
        if (ChangeLogLinter.DDL_CHANGE_TYPES.contains(change.getClass())) {
            for (String context : contexts) {
                if (!getRuleConfig().getPattern().map(pattern -> pattern.matcher(context).matches()).orElse(true)) {
                    return true;
                }
            }
        } else if (ChangeLogLinter.DML_CHANGE_TYPES.contains(change.getClass())) {
            for (String context : contexts) {
                if (getRuleConfig().getPattern().map(pattern -> pattern.matcher(context).matches()).orElse(false)) {
                    return true;
                }
            }
        }
        return false;
    }

}
