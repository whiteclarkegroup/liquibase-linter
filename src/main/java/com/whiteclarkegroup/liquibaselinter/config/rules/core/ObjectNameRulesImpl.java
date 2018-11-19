package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.AbstractChange;
import liquibase.change.ColumnConfig;
import liquibase.change.core.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectNameRulesImpl {

    private static boolean doesSupport(AbstractChange change) {
        return change instanceof AddColumnChange
            || change instanceof AddForeignKeyConstraintChange
            || change instanceof AddPrimaryKeyChange
            || change instanceof AddUniqueConstraintChange
            || change instanceof CreateTableChange
            || change instanceof MergeColumnChange
            || change instanceof RenameColumnChange
            || change instanceof RenameViewChange;
    }

    private static Collection<String> getObjectNames(AbstractChange change) {
        if (change instanceof AddColumnChange) {
            return ((AddColumnChange) change).getColumns().stream().map(ColumnConfig::getName).collect(Collectors.toList());
        } else if (change instanceof AddForeignKeyConstraintChange) {
            return Collections.singletonList(((AddForeignKeyConstraintChange) change).getConstraintName());
        } else if (change instanceof AddPrimaryKeyChange) {
            return Collections.singletonList(((AddPrimaryKeyChange) change).getConstraintName());
        } else if (change instanceof AddUniqueConstraintChange) {
            return Collections.singletonList(((AddUniqueConstraintChange) change).getConstraintName());
        } else if (change instanceof CreateTableChange) {
            return ((CreateTableChange) change).getColumns().stream().map(ColumnConfig::getName).collect(Collectors.toList());
        } else if (change instanceof MergeColumnChange) {
            return Collections.singletonList(((MergeColumnChange) change).getFinalColumnName());
        } else if (change instanceof RenameColumnChange) {
            return Collections.singletonList(((RenameColumnChange) change).getNewColumnName());
        } else if (change instanceof RenameViewChange) {
            return Collections.singletonList(((RenameViewChange) change).getNewViewName());
        }
        return Collections.emptyList();
    }

    public static class ObjectNameRuleImpl extends AbstractLintRule implements ChangeRule<AbstractChange> {
        private static final String NAME = "object-name";
        private static final String MESSAGE = "Object name does not follow pattern";

        public ObjectNameRuleImpl() {
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
            return getObjectNames(change).stream().anyMatch(objectName -> checkPattern(objectName, change));
        }

        @Override
        public String getMessage(AbstractChange change) {
            Optional<String> first = getObjectNames(change).stream().filter(objectName -> checkPattern(objectName, change)).findFirst();
            return formatMessage(first.get());
        }

    }

    public static class ObjectNameLengthRuleImpl extends AbstractLintRule implements ChangeRule<AbstractChange> {
        private static final String NAME = "object-name-length";
        private static final String MESSAGE = "Object name '%s' must be less than %d characters";

        public ObjectNameLengthRuleImpl() {
            super(NAME, MESSAGE);
        }

        @Override
        public Class<AbstractChange> getChangeType() {
            return AbstractChange.class;
        }

        @Override
        public boolean supports(AbstractChange change) {
            return doesSupport(change) || change instanceof CreateIndexChange;
        }

        @Override
        public boolean invalid(AbstractChange change) {
            if (change instanceof CreateIndexChange) {
                return checkMaxLength(((CreateIndexChange) change).getIndexName());
            } else {
                return getObjectNames(change).stream().anyMatch(this::checkMaxLength);
            }
        }

        @Override
        public String getMessage(AbstractChange change) {
            if (change instanceof CreateIndexChange) {
                return formatMessage(((CreateIndexChange) change).getIndexName(), getConfig().getMaxLength());
            } else {
                Optional<String> first = getObjectNames(change).stream().filter(this::checkMaxLength).findFirst();
                return formatMessage(first.get(), getConfig().getMaxLength());
            }
        }
    }
}
