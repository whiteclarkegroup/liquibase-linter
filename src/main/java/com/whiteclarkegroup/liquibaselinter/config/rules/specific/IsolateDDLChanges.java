package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import com.whiteclarkegroup.liquibaselinter.ChangeLogLinter;
import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.Change;

import java.util.List;
import java.util.stream.Collectors;

public class IsolateDDLChanges extends Rule<List<Change>> {

    public IsolateDDLChanges(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(List<Change> changes, Change change) {
        return changes.stream().filter(cng -> ChangeLogLinter.DDL_CHANGE_TYPES.contains(cng.getClass())).collect(Collectors.toList()).size() > 1;
    }

}
