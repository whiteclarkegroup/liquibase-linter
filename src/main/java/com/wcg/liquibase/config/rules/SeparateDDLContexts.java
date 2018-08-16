package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;

import java.util.Collection;

import static com.wcg.liquibase.ChangeLogLinter.DDL_CHANGE_TYPES;
import static com.wcg.liquibase.ChangeLogLinter.DML_CHANGE_TYPES;

public class SeparateDDLContexts extends PatternRule {

    public SeparateDDLContexts(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        Collection<String> contexts = (Collection<String>) object;
        if (DDL_CHANGE_TYPES.contains(change.getClass())) {
            for (String context : contexts) {
                if (!getRuleConfig().getPattern().map(pattern -> pattern.matcher(context).matches()).orElse(true)) {
                    return true;
                }
            }
        } else if (DML_CHANGE_TYPES.contains(change.getClass())) {
            for (String context : contexts) {
                if (getRuleConfig().getPattern().map(pattern -> pattern.matcher(context).matches()).orElse(false)) {
                    return true;
                }
            }
        }
        return false;
    }

}
