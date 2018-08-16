package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

public class AddColumnChangeLinter implements Linter<AddColumnChange> {

    private ColumnConfigLinter columnConfigLinter = new ColumnConfigLinter();
    private ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddColumnChange change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        for (AddColumnConfig addColumnConfig : change.getColumns()) {
            getObjectNameLinter().lintObjectName(addColumnConfig.getName(), change, ruleConfigs);
        }
        getColumnConfigLinter().lintColumnConfig(change, ruleConfigs);
    }

    ColumnConfigLinter getColumnConfigLinter() {
        return columnConfigLinter;
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
