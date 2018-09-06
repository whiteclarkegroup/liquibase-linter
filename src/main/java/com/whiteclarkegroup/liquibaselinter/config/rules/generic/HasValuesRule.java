package com.whiteclarkegroup.liquibaselinter.config.rules.generic;

import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.Change;

import java.util.Collection;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

public class HasValuesRule extends Rule<Collection<Object>> {

    public HasValuesRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(Collection<Object> collection, Change change) {
        return isEmpty(collection);
    }

}
