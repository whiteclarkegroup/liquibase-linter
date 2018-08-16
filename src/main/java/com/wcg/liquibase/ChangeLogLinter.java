package com.wcg.liquibase;

import com.google.common.collect.ImmutableList;
import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.Change;
import liquibase.change.core.*;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
                    .build();
    private static final Pattern LINT_IGNORE = Pattern.compile(".*lql-ignore.*");

    @SuppressWarnings("unchecked")
    public void lintChangeLog(final DatabaseChangeLog databaseChangeLog, Config config) throws ChangeLogParseException {
        RuleRunner.forDatabaseChangeLog(databaseChangeLog).run(config.getRules().getFileNameNoSpaces(), databaseChangeLog);
        lintChangeSets(databaseChangeLog, config);
    }

    private void lintChangeSets(DatabaseChangeLog databaseChangeLog, Config config) throws ChangeLogParseException {
        final List<ChangeSet> changeSets = databaseChangeLog.getChangeSets();
        for (ChangeSet changeSet : changeSets) {
            if (isIgnorable(changeSet, config)) {
                continue;
            }
            RuleRunner ruleRunner = RuleRunner.forDatabaseChangeLog(databaseChangeLog);
            ruleRunner.run(config.getRules().getNoPreconditions(), changeSet::getPreconditions);
            Collection<? extends Object> contexts = changeSet.getContexts() != null ? changeSet.getContexts().getContexts() : new HashSet<>();
            ruleRunner.run(config.getRules().getHasContext(), changeSet.getContexts() != null ? changeSet.getContexts().getContexts() : new HashSet<>())
                    .run(config.getRules().getHasComment(), changeSet::getComments);

            final List<Change> changes = changeSet.getChanges();
            ruleRunner.run(config.getRules().getIsolateDDLChanges(), changes);
            for (Change change : changes) {
                RuleRunner.forChange(change)
                        .run(config.getRules().getValidContext(), contexts)
                        .run(config.getRules().getSeparateDdlContexts(), contexts);
                lint(change, config);
            }

        }
    }

    private boolean isIgnorable(ChangeSet changeSet, Config config) {
        return isIgnorableContext(changeSet, config) || hasIgnoreComment(changeSet);
    }

    private boolean isIgnorableContext(ChangeSet changeSet, Config config) {
        if (config.getIgnoreContextPattern() != null && changeSet.getContexts() != null) {
            return changeSet.getContexts().getContexts().stream()
                    .allMatch(context -> config.getIgnoreContextPattern().matcher(context).matches());
        }
        return false;
    }

    private boolean hasIgnoreComment(ChangeSet changeSet) {
        return changeSet.getComments() != null && LINT_IGNORE.matcher(changeSet.getComments()).matches();
    }

    @SuppressWarnings("unchecked")
    private void lint(Change change, Config config) throws ChangeLogParseException {
        final Optional<Linter> linter = LinterFactory.getLinter(change);
        if (linter.isPresent()) {
            linter.get().lint(change, config.getRules());
        }
    }

}
