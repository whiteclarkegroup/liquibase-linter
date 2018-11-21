package com.whiteclarkegroup.liquibaselinter.config.rules.core;


import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.AbstractChange;
import liquibase.change.core.AbstractModifyDataChange;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.AddDefaultValueChange;

public class SchemaNameRulesImpl {

    private static boolean doesSupport(AbstractChange change) {
        return change instanceof AbstractModifyDataChange
            || change instanceof AddColumnChange
            || change instanceof AddDefaultValueChange;
    }

    private static String getSchemaName(AbstractChange change) {
        if (change instanceof AbstractModifyDataChange) {
            return ((AbstractModifyDataChange) change).getSchemaName();
        } else if (change instanceof AddColumnChange) {
            return ((AddColumnChange) change).getSchemaName();
        } else if (change instanceof AddDefaultValueChange) {
            return ((AddDefaultValueChange) change).getSchemaName();
        }
        return null;
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
            return checkPattern(getSchemaName(change), change);
        }

        @Override
        public String getMessage(AbstractChange change) {
            return formatMessage(getSchemaName(change), getConfig().getPatternString());
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
        public boolean supports(AbstractChange change) {
            return doesSupport(change);
        }

        @Override
        public boolean invalid(AbstractChange change) {
            return checkNotBlank(getSchemaName(change));
        }

    }

}
