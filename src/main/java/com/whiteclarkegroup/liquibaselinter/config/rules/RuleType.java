package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.whiteclarkegroup.liquibaselinter.config.rules.generic.*;
import com.whiteclarkegroup.liquibaselinter.config.rules.specific.*;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public enum RuleType {

    NO_DUPLICATE_INCLUDES("no-duplicate-includes", GenericRule::new, "Changelog file '%s' was included more than once"),
    SCHEMA_NAME("schema-name", PatternRule::new, "Schema name does not follow pattern"),
    TABLE_NAME_LENGTH("table-name-length", MaxLengthRule::new, "Table '%s' name must not be longer than %d"),
    TABLE_NAME("table-name", PatternRule::new, "Table name does not follow pattern"),
    OBJECT_NAME("object-name", PatternRule::new, "Object name does not follow pattern"),
    OBJECT_NAME_LENGTH("object-name-length", MaxLengthRule::new, "Object name '%s' must be less than %d characters"),
    CREATE_TABLE_REMARKS("create-table-remarks", NotBlankRule::new, "Create table must contain remark attribute"),
    CREATE_COLUMN_REMARKS("create-column-remarks", NotBlankRule::new, "Add column must contain remarks"),
    CREATE_COLUMN_NULLABLE_CONSTRAINT("create-column-nullable-constraint", CreateColumnNullableConstraint::new, "Add column must specify nullable constraint"),
    CREATE_COLUMN_NO_DEFINE_PRIMARY_KEY("create-column-no-define-primary-key", CreateColumnNoDefinePrimaryKey::new, "Add column must not use primary key attribute. Instead use AddPrimaryKey change type"),
    MODIFY_DATA_ENFORCE_WHERE("modify-data-enforce-where", ModifyDataEnforceWhere::new, "Modify data on table '%s' must have a where condition"),
    CREATE_INDEX_NAME("create-index-name", MandatoryPatternRule::new, "Index name does not follow pattern"),
    UNIQUE_CONSTRAINT_NAME("unique-constraint-name", MandatoryPatternRule::new, "Unique constraint name does not follow pattern"),
    FOREIGN_KEY_NAME("foreign-key-name", MandatoryPatternRule::new, "Foreign key name is missing or does not follow pattern"),
    FILE_NAME_NO_SPACES("file-name-no-spaces", FileNameNoSpaces::new, "Changelog filenames should not contain spaces"),
    HAS_COMMENT("has-comment", NotBlankRule::new, "Change set must have a comment"),
    HAS_CONTEXT("has-context", HasValuesRule::new, "Should have at least one context on the change set"),
    ISOLATE_DDL_CHANGES("isolate-ddl-changes", IsolateDDLChanges::new, "Should only have a single ddl change per change set"),
    VALID_CONTEXT("valid-context", ValidContext::new, "Context does not follow pattern"),
    SEPARATE_DDL_CONTEXT("separate-ddl-context", SeparateDDLContexts::new, "Should have a ddl changes under ddl contexts"),
    MODIFY_DATA_STARTS_WITH_WHERE("modify-data-starts-with-where", ModifyDataStartsWithWhere::new, "Modify data where starts with where clause, that's probably a mistake"),
    DROP_NOT_NULL_REQUIRE_COLUMN_DATA_TYPE("drop-not-null-require-column-data-type", NotBlankRule::new, "Drop not null constraint column data type attribute must be populated");

    private String key;
    private Function<RuleConfig, Rule> factory;
    private String defaultErrorMessage;

    RuleType(String key, Function<RuleConfig, Rule> factory, String defaultErrorMessage) {
        this.key = key;
        this.factory = factory;
        this.defaultErrorMessage = defaultErrorMessage;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultErrorMessage() {
        return defaultErrorMessage;
    }

    public Optional<Rule> create(Map<String, RuleConfig> ruleConfigs) {
        if (!ruleConfigs.containsKey(key)) {
            return Optional.empty();
        }
        final RuleConfig ruleConfig = ruleConfigs.get(key);
        if (ruleConfig != null) {
            return Optional.of(this.factory.apply(ruleConfig));
        }
        return Optional.of(this.factory.apply(RuleConfig.enabled()));
    }
}
