package com.wcg.liquibase.config.rules;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.generic.*;
import com.wcg.liquibase.config.rules.specific.*;

import java.util.Map;
import java.util.function.Function;

public enum RuleType {

    NO_DUPLICATE_INCLUDES("no-duplicate-includes", GenericRule::new),
    SCHEMA_NAME("schema-name", PatternRule::new),
    TABLE_NAME_LENGTH("table-name-length", MaxLengthRule::new),
    TABLE_NAME("table-name", PatternRule::new),
    OBJECT_NAME("object-name", PatternRule::new),
    OBJECT_NAME_LENGTH("object-name-length", MaxLengthRule::new),
    CREATE_TABLE_REMARKS("create-table-remarks", NotBlankRule::new),
    CREATE_COLUMN_REMARKS("create-column-remarks", NotBlankRule::new),
    CREATE_COLUMN_NULLABLE_CONSTRAINT("create-column-nullable-constraint", CreateColumnNullableConstraint::new),
    CREATE_COLUMN_NO_DEFINE_PRIMARY_KEY("create-column-no-define-primary-key", CreateColumnNoDefinePrimaryKey::new),
    MODIFY_DATA_ENFORCE_WHERE("modify-data-enforce-where", ModifyDataEnforceWhere::new),
    CREATE_INDEX_NAME("create-index-name", PatternRule::new),
    UNIQUE_CONSTRAINT_NAME("unique-constraint-name", PatternRule::new),
    PRIMARY_KEY_MUST_BE_NAMED("primary-key-must-be-named", PatternRule::new),
    PRIMARY_KEY_MUST_USE_TABLE_NAME("primary-key-must-use-table-name", PatternRule::new),
    FOREIGN_KEY_MUST_BE_NAMED("foreign-key-must-be-named", PatternRule::new),
    FOREIGN_KEY_MUST_USE_BASE_AND_REFERENCE_TABLE_NAME("foreign-key-must-use-base-and-referenced-table-name", PatternRule::new),
    FILE_NAME_NO_SPACES("file-name-no-spaces", FileNameNoSpaces::new),
    NO_PRECONDITIONS("no-preconditions", NullRule::new),
    HAS_COMMENT("has-comment", NotBlankRule::new),
    HAS_CONTEXT("has-context", HasValuesRule::new),
    ISOLATE_DDL_CHANGES("isolate-ddl-changes", IsolateDDLChanges::new),
    VALID_CONTEXT("valid-context", ValidContext::new),
    SEPARATE_DDL_CONTEXT("separate-ddl-context", SeparateDDLContexts::new),
    MODIFY_DATA_STARTS_WITH_WHERE("modify-data-starts-with-where", ModifyDataStartsWithWhere::new),
    DROP_NOT_NULL_REQUIRE_COLUMN_DATA_TYPE("drop-not-null-require-column-data-type", NotBlankRule::new);

    private String key;
    private Function<RuleConfig, Rule> factory;

    RuleType(String key, Function<RuleConfig, Rule> factory) {
        this.key = key;
        this.factory = factory;
    }

    public String getKey() {
        return key;
    }

    public Rule create(Map<String, RuleConfig> ruleConfigs) {
        return this.factory.apply(ruleConfigs.getOrDefault(key, RuleConfig.disabled()));
    }
}
