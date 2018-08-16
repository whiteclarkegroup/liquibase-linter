package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.Rules;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.exception.ChangeLogParseException;

public class AddColumnChangeLinter implements Linter<AddColumnChange> {

    private ColumnConfigLinter columnConfigLinter = new ColumnConfigLinter();
    private ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddColumnChange change, Rules rules) throws ChangeLogParseException {
        for (AddColumnConfig addColumnConfig : change.getColumns()) {
            getObjectNameLinter().lintObjectName(addColumnConfig.getName(), change, rules);
        }
        getColumnConfigLinter().lintColumnConfig(change, rules);
    }

    ColumnConfigLinter getColumnConfigLinter() {
        return columnConfigLinter;
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
