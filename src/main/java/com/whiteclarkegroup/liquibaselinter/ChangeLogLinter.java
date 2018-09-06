package com.whiteclarkegroup.liquibaselinter;

import com.google.common.collect.ImmutableList;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import liquibase.change.Change;
import liquibase.change.core.*;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;

import java.util.Collection;
import java.util.Collections;
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
                    .add(LoadDataChange.class)
                    .add(LoadUpdateDataChange.class)
                    .build();
    private static final Pattern LINT_IGNORE = Pattern.compile(".*lql-ignore.*");

    @SuppressWarnings("unchecked")
    public void lintChangeLog(final DatabaseChangeLog databaseChangeLog, Config config, RuleRunner ruleRunner) throws ChangeLogParseException {
        ruleRunner.forDatabaseChangeLog(databaseChangeLog).run(RuleType.FILE_NAME_NO_SPACES, databaseChangeLog);
        lintChangeSets(databaseChangeLog, config, ruleRunner);
    }

    private void lintChangeSets(DatabaseChangeLog databaseChangeLog, Config config, RuleRunner ruleRunner) throws ChangeLogParseException {
        final List<ChangeSet> changeSets = databaseChangeLog.getChangeSets();
        for (ChangeSet changeSet : changeSets) {
            if (isIgnorable(changeSet, config)) {
                continue;
            }

            Collection<String> contexts = changeSet.getContexts() != null ? changeSet.getContexts().getContexts() : Collections.emptySet();
            List<Change> changes = changeSet.getChanges();

            ruleRunner.forDatabaseChangeLog(databaseChangeLog)
                    .run(RuleType.NO_PRECONDITIONS, changeSet.getPreconditions())
                    .run(RuleType.HAS_CONTEXT, contexts)
                    .run(RuleType.HAS_COMMENT, changeSet.getComments())
                    .run(RuleType.ISOLATE_DDL_CHANGES, changes);

            for (Change change : changes) {
                isIllegalChangeType(config, change);
                ruleRunner.forChange(change)
                        .run(RuleType.VALID_CONTEXT, contexts)
                        .run(RuleType.SEPARATE_DDL_CONTEXT, contexts);
                lint(change, ruleRunner);
            }

        }
    }

    private void isIllegalChangeType(Config config, Change change) throws ChangeLogParseException {
        if (config.getIllegalChangeTypes() != null) {
            for (Class illegal : config.getIllegalChangeTypes()) {
                if (change.getClass() == illegal) {
                    final String errorMessage = String.format("Change type '%s' is not allowed in this project", illegal.getCanonicalName());
                    throw ChangeLogParseExceptionHelper.build(change.getChangeSet().getChangeLog(), change, errorMessage);
                }
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
        return changeSet.getComments() != null && LINT_IGNORE.matcher(changeSet.getComments()).matches();
    }

    @SuppressWarnings("unchecked")
    private void lint(Change change, RuleRunner ruleRunner) throws ChangeLogParseException {
        final Optional<Linter> linter = LinterFactory.getLinter(change);
        if (linter.isPresent()) {
            linter.get().lint(change, ruleRunner);
        }
    }

}
