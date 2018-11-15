package com.whiteclarkegroup.liquibaselinter;

import com.google.common.collect.ImmutableList;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import liquibase.change.Change;
import liquibase.change.core.*;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.List;
import java.util.Optional;

public class ChangeLogLinter {

    public static final List<Class> DDL_CHANGE_TYPES =
            ImmutableList.<Class>builder()
                    .add(DropViewChange.class)
                    .add(AddUniqueConstraintChange.class)
                    .add(DropColumnChange.class)
                    .add(DropIndexChange.class)
                    .add(AddForeignKeyConstraintChange.class)
                    .add(ModifyDataTypeChange.class)
                    .add(DropNotNullConstraintChange.class)
                    .add(RenameTableChange.class)
                    .add(MergeColumnChange.class)
                    .add(AlterSequenceChange.class)
                    .add(CreateIndexChange.class)
                    .add(RenameViewChange.class)
                    .add(DropPrimaryKeyChange.class)
                    .add(DropUniqueConstraintChange.class)
                    .add(DropSequenceChange.class)
                    .add(RenameSequenceChange.class)
                    .add(CreateSequenceChange.class)
                    .add(AddNotNullConstraintChange.class)
                    .add(DropDefaultValueChange.class)
                    .add(AddColumnChange.class)
                    .add(DropTableChange.class)
                    .add(DropAllForeignKeyConstraintsChange.class)
                    .add(CreateViewChange.class)
                    .add(CreateTableChange.class)
                    .add(RenameColumnChange.class)
                    .add(CreateProcedureChange.class)
                    .add(DropForeignKeyConstraintChange.class)
                    .add(DropProcedureChange.class)
                    .add(AddPrimaryKeyChange.class)
                    .add(AddDefaultValueChange.class)
                    .build();
    public static final List<Class> DML_CHANGE_TYPES =
            ImmutableList.<Class>builder()
                    .add(InsertDataChange.class)
                    .add(UpdateDataChange.class)
                    .add(DeleteDataChange.class)
                    .add(LoadDataChange.class)
                    .add(LoadUpdateDataChange.class)
                    .build();

    @SuppressWarnings("unchecked")
    public void lintChangeLog(final DatabaseChangeLog databaseChangeLog, Config config, RuleRunner ruleRunner) throws ChangeLogParseException {
        ruleRunner.forDatabaseChangeLog(databaseChangeLog)
            .checkChangeLog();
        lintChangeSets(databaseChangeLog, config, ruleRunner);
    }

    private void lintChangeSets(DatabaseChangeLog databaseChangeLog, Config config, RuleRunner ruleRunner) throws ChangeLogParseException {
        final List<ChangeSet> changeSets = databaseChangeLog.getChangeSets();
        for (ChangeSet changeSet : changeSets) {
            if (isIgnorable(changeSet, config)) {
                continue;
            }

            List<Change> changes = changeSet.getChanges();

            ruleRunner.forChangeSet(changeSet).checkChangeSet();

            for (Change change : changes) {
                ruleRunner.forChange(change).checkChange();
                lint(change, ruleRunner);
            }

        }
    }

    private boolean isIgnorable(ChangeSet changeSet, Config config) {
        return isIgnorableContext(changeSet, config) || hasIgnoreComment(changeSet);
    }

    private boolean isIgnorableContext(ChangeSet changeSet, Config config) {
        if (config.getIgnoreContextPattern() != null && changeSet.getContexts() != null && !changeSet.getContexts().getContexts().isEmpty()) {
            return changeSet.getContexts().getContexts().stream()
                    .allMatch(context -> config.getIgnoreContextPattern().matcher(context).matches());
        }
        return false;
    }

    private boolean hasIgnoreComment(ChangeSet changeSet) {
        return changeSet.getComments() != null && changeSet.getComments().endsWith("lql-ignore");
    }

    @SuppressWarnings("unchecked")
    private void lint(Change change, RuleRunner ruleRunner) throws ChangeLogParseException {
        final Optional<Linter> linter = LinterFactory.getLinter(change);
        if (linter.isPresent()) {
            linter.get().lint(change, ruleRunner);
        }
    }

}
