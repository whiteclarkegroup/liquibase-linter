package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;

import java.util.Collection;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

public class HasValuesRule extends Rule<Collection<? extends Object>> {

    public HasValuesRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Collection<? extends Object> collection, Change change) {
        return isEmpty(collection);
    }

}
