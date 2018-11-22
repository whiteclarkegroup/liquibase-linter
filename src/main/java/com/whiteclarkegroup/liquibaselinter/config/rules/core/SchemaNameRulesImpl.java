package com.whiteclarkegroup.liquibaselinter.config.rules.core;


import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.AbstractChange;
import liquibase.change.core.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class SchemaNameRulesImpl {

    private static boolean doesSupport(AbstractChange change) {
        return !getSchemaName(change).isEmpty();
    }

    private static Collection<String> getSchemaName(AbstractChange change) {
        if (change instanceof AbstractModifyDataChange) {
            return Collections.singletonList(((AbstractModifyDataChange) change).getSchemaName());
        } else if (change instanceof AddAutoIncrementChange) {
            return Collections.singletonList(((AddAutoIncrementChange) change).getSchemaName());
        } else if (change instanceof AddColumnChange) {
            return Collections.singletonList(((AddColumnChange) change).getSchemaName());
        } else if (change instanceof AddDefaultValueChange) {
            return Collections.singletonList(((AddDefaultValueChange) change).getSchemaName());
        } else if (change instanceof AddForeignKeyConstraintChange) {
            return Arrays.asList(((AddForeignKeyConstraintChange) change).getBaseTableSchemaName(), ((AddForeignKeyConstraintChange) change).getReferencedTableSchemaName());
        } else if (change instanceof AddLookupTableChange) {
            return Arrays.asList(((AddLookupTableChange) change).getExistingTableSchemaName(), ((AddLookupTableChange) change).getNewTableSchemaName());
        } else if (change instanceof AddNotNullConstraintChange) {
            return Collections.singletonList(((AddNotNullConstraintChange) change).getSchemaName());
        } else if (change instanceof AddPrimaryKeyChange) {
            return Collections.singletonList(((AddPrimaryKeyChange) change).getSchemaName());
        } else if (change instanceof AddUniqueConstraintChange) {
            return Collections.singletonList(((AddUniqueConstraintChange) change).getSchemaName());
        } else if (change instanceof AlterSequenceChange) {
            return Collections.singletonList(((AlterSequenceChange) change).getSchemaName());
        } else if (change instanceof CreateIndexChange) {
            return Collections.singletonList(((CreateIndexChange) change).getSchemaName());
        } else if (change instanceof CreateProcedureChange) {
            return Collections.singletonList(((CreateProcedureChange) change).getSchemaName());
        } else if (change instanceof CreateSequenceChange) {
            return Collections.singletonList(((CreateSequenceChange) change).getSchemaName());
        } else if (change instanceof CreateTableChange) {
            return Collections.singletonList(((CreateTableChange) change).getSchemaName());
        } else if (change instanceof CreateViewChange) {
            return Collections.singletonList(((CreateViewChange) change).getSchemaName());
        } else if (change instanceof DropAllForeignKeyConstraintsChange) {
            return Collections.singletonList(((DropAllForeignKeyConstraintsChange) change).getBaseTableSchemaName());
        } else if (change instanceof DropColumnChange) {
            return Collections.singletonList(((DropColumnChange) change).getSchemaName());
        } else if (change instanceof DropDefaultValueChange) {
            return Collections.singletonList(((DropDefaultValueChange) change).getSchemaName());
        } else if (change instanceof DropForeignKeyConstraintChange) {
            return Collections.singletonList(((DropForeignKeyConstraintChange) change).getBaseTableSchemaName());
        } else if (change instanceof DropIndexChange) {
            return Collections.singletonList(((DropIndexChange) change).getSchemaName());
        } else if (change instanceof DropNotNullConstraintChange) {
            return Collections.singletonList(((DropNotNullConstraintChange) change).getSchemaName());
        } else if (change instanceof DropPrimaryKeyChange) {
            return Collections.singletonList(((DropPrimaryKeyChange) change).getSchemaName());
        } else if (change instanceof DropProcedureChange) {
            return Collections.singletonList(((DropProcedureChange) change).getSchemaName());
        } else if (change instanceof DropSequenceChange) {
            return Collections.singletonList(((DropSequenceChange) change).getSchemaName());
        } else if (change instanceof DropTableChange) {
            return Collections.singletonList(((DropTableChange) change).getSchemaName());
        } else if (change instanceof DropUniqueConstraintChange) {
            return Collections.singletonList(((DropUniqueConstraintChange) change).getSchemaName());
        } else if (change instanceof DropViewChange) {
            return Collections.singletonList(((DropViewChange) change).getSchemaName());
        } else if (change instanceof InsertDataChange) {
            return Collections.singletonList(((InsertDataChange) change).getSchemaName());
        } else if (change instanceof LoadDataChange) {
            return Collections.singletonList(((LoadDataChange) change).getSchemaName());
        } else if (change instanceof MergeColumnChange) {
            return Collections.singletonList(((MergeColumnChange) change).getSchemaName());
        } else if (change instanceof ModifyDataTypeChange) {
            return Collections.singletonList(((ModifyDataTypeChange) change).getSchemaName());
        } else if (change instanceof RenameColumnChange) {
            return Collections.singletonList(((RenameColumnChange) change).getSchemaName());
        } else if (change instanceof RenameSequenceChange) {
            return Collections.singletonList(((RenameSequenceChange) change).getSchemaName());
        } else if (change instanceof RenameTableChange) {
            return Collections.singletonList(((RenameTableChange) change).getSchemaName());
        } else if (change instanceof RenameViewChange) {
            return Collections.singletonList(((RenameViewChange) change).getSchemaName());
        } else if (change instanceof SetColumnRemarksChange) {
            return Collections.singletonList(((SetColumnRemarksChange) change).getSchemaName());
        } else if (change instanceof SetTableRemarksChange) {
            return Collections.singletonList(((SetTableRemarksChange) change).getSchemaName());
        }
        return Collections.emptyList();
    }

    public static class SchemaNameRuleImpl extends AbstractLintRule implements ChangeRule<AbstractChange> {

        private static final String NAME = "schema-name";
        private static final String MESSAGE = "Schema name '%s' does not follow pattern '%s'";

        public SchemaNameRuleImpl() {
            super(NAME, MESSAGE);
        }

        @Override
        public Class<AbstractChange> getChangeType() {
            return AbstractChange.class;
        }

        @Override
        public boolean supports(AbstractChange change) {
            return doesSupport(change);
        }

        @Override
        public boolean invalid(AbstractChange change) {
            return getSchemaName(change).stream().anyMatch(schemaName -> checkPattern(schemaName, change));
        }

        @Override
        public String getMessage(AbstractChange change) {
            String joined = getSchemaName(change).stream().filter(schemaName -> checkMandatoryPattern(schemaName, change)).collect(Collectors.joining(","));
            return formatMessage(joined, getConfig().getPatternString());
        }

    }

    public static class NoSchemaNameRuleImpl extends AbstractLintRule implements ChangeRule<AbstractChange> {

        private static final String NAME = "no-schema-name";
        private static final String MESSAGE = "Schema names are not allowed in this project";

        public NoSchemaNameRuleImpl() {
            super(NAME, MESSAGE);
        }

        @Override
        public Class<AbstractChange> getChangeType() {
            return AbstractChange.class;
        }

        @Override
        public boolean invalid(AbstractChange change) {
            return getSchemaName(change).stream().anyMatch(this::checkBlank);
        }
    }

}
