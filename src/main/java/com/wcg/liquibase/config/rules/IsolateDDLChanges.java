package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.Change;

import java.util.List;
import java.util.stream.Collectors;

import static com.wcg.liquibase.ChangeLogLinter.DDL_CHANGE_TYPES;

public class IsolateDDLChanges extends Rule<List<Change>> {

    public IsolateDDLChanges(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    @Override
    public boolean invalid(List<Change> changes, Change change) {
        return changes.stream().filter(cng -> DDL_CHANGE_TYPES.contains(cng.getClass())).collect(Collectors.toList()).size() > 1;
    }

}
