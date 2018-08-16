package com.wcg.liquibase.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wcg.liquibase.config.rules.Rule;
import com.wcg.liquibase.config.rules.generic.*;
import com.wcg.liquibase.config.rules.specific.*;

import static java.util.Optional.ofNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rules {
    private final GenericRule noDuplicateIncludes;
    private final PatternRule schemaName;
    private final MaxLengthRule tableNameLength;
    private final PatternRule tableName;
    private final PatternRule objectName;
    private final MaxLengthRule objectNameLength;
    private final NotBlankRule createTableRemarks;
    private final NotBlankRule createColumnRemarks;
    private final CreateColumnNullableConstraint createColumnNullableConstraint;
    private final CreateColumnNoDefinePrimaryKey createColumnNoDefinePrimaryKey;
    private final ModifyDataEnforceWhere modifyDataEnforceWhere;
    private final PatternRule createIndexName;
    private final PatternRule uniqueConstraintNameRule;
    private final PatternRule primaryKeyName;
    private final PatternRule primaryKeyMustUseTableName;
    private final PatternRule foreignKeyName;
    private final PatternRule foreignKeyTableAndReferencedTableNameRule;
    private final FileNameNoSpaces fileNameNoSpaces;
    private final NullRule noPreconditions;
    private final NotBlankRule hasComment;
    private final HasValuesRule hasContext;
    private final IsolateDDLChanges isolateDDLChanges;
    private final ValidContext validContext;
    private final SeparateDDLContexts separateDdlContexts;
    private final ModifyDataStartsWithWhere modifyDataStartsWithWhere;

    @JsonCreator
    public Rules(@JsonProperty("no-duplicate-includes") RuleConfig noDuplicateIncludes,
                 @JsonProperty("schema-name") RuleConfig schemaName,
                 @JsonProperty("table-name-length") RuleConfig tableNameLength,
                 @JsonProperty("table-name") RuleConfig tableName,
                 @JsonProperty("object-name") RuleConfig objectName,
                 @JsonProperty("object-name-length") RuleConfig objectNameLength,
                 @JsonProperty("create-table-remarks") RuleConfig createTableRemarks,
                 @JsonProperty("create-column-remarks") RuleConfig createColumnRemarks,
                 @JsonProperty("create-column-nullable-constraint") RuleConfig createColumnNullableConstraint,
                 @JsonProperty("create-column-no-define-primary-key") RuleConfig createColumnNoDefinePrimaryKey,
                 @JsonProperty("modify-data-enforce-where") RuleConfig modifyDataEnforceWhere,
                 @JsonProperty("create-index-name") RuleConfig createIndexName,
                 @JsonProperty("unique-constraint-name") RuleConfig uniqueConstraintNameRule,
                 @JsonProperty("primary-key-must-be-named") RuleConfig primaryKeyName,
                 @JsonProperty("primary-key-must-use-table-name") RuleConfig primaryKeyMustUseTableName,
                 @JsonProperty("foreign-key-must-be-named") RuleConfig foreignKeyName,
                 @JsonProperty("foreign-key-must-use-base-and-referenced-table-name") RuleConfig foreignKeyTableAndReferencedTableNameRule,
                 @JsonProperty("file-name-no-spaces") RuleConfig fileNameNoSpaces,
                 @JsonProperty("no-preconditions") RuleConfig noPreconditions,
                 @JsonProperty("has-comment") RuleConfig hasComment,
                 @JsonProperty("has-context") RuleConfig hasContext,
                 @JsonProperty("isolate-ddl-changes") RuleConfig isolateDDLChanges,
                 @JsonProperty("valid-context") RuleConfig validContext,
                 @JsonProperty("separate-ddl-context") RuleConfig separateDdlContexts,
                 @JsonProperty("modify-data-starts-with-where") RuleConfig modifyDataStartsWithWhere) {
        this.noDuplicateIncludes = new GenericRule(noDuplicateIncludes);
        this.schemaName = new PatternRule(schemaName);
        this.tableNameLength = new MaxLengthRule(tableNameLength);
        this.tableName = new PatternRule(tableName);
        this.objectName = new PatternRule(objectName);
        this.objectNameLength = new MaxLengthRule(objectNameLength);
        this.createTableRemarks = new NotBlankRule(createTableRemarks);
        this.createColumnRemarks = new NotBlankRule(createColumnRemarks);
        this.createColumnNullableConstraint = new CreateColumnNullableConstraint(createColumnNullableConstraint);
        this.createColumnNoDefinePrimaryKey = new CreateColumnNoDefinePrimaryKey(createColumnNoDefinePrimaryKey);
        this.modifyDataEnforceWhere = new ModifyDataEnforceWhere(modifyDataEnforceWhere);
        this.createIndexName = new PatternRule(createIndexName);
        this.uniqueConstraintNameRule = new PatternRule(uniqueConstraintNameRule);
        this.primaryKeyName = new PatternRule(primaryKeyName);
        this.primaryKeyMustUseTableName = new PatternRule(primaryKeyMustUseTableName);
        this.foreignKeyName = new PatternRule(foreignKeyName);
        this.foreignKeyTableAndReferencedTableNameRule = new PatternRule(foreignKeyTableAndReferencedTableNameRule);
        this.fileNameNoSpaces = new FileNameNoSpaces(fileNameNoSpaces);
        this.noPreconditions = new NullRule(noPreconditions);
        this.hasComment = new NotBlankRule(hasComment);
        this.hasContext = new HasValuesRule(hasContext);
        this.isolateDDLChanges = new IsolateDDLChanges(isolateDDLChanges);
        this.validContext = new ValidContext(validContext);
        this.separateDdlContexts = new SeparateDDLContexts(separateDdlContexts);
        this.modifyDataStartsWithWhere = new ModifyDataStartsWithWhere(modifyDataStartsWithWhere);
    }

    public GenericRule getNoDuplicateIncludes() {
        return noDuplicateIncludes;
    }

    public PatternRule getSchemaName() {
        return schemaName;
    }

    public MaxLengthRule getTableNameLength() {
        return tableNameLength;
    }

    public PatternRule getTableName() {
        return tableName;
    }

    public PatternRule getObjectName() {
        return objectName;
    }

    public MaxLengthRule getObjectNameLength() {
        return objectNameLength;
    }

    public NotBlankRule getCreateTableRemarks() {
        return createTableRemarks;
    }

    public NotBlankRule getCreateColumnRemarks() {
        return createColumnRemarks;
    }

    public CreateColumnNullableConstraint getCreateColumnNullableConstraint() {
        return createColumnNullableConstraint;
    }

    public CreateColumnNoDefinePrimaryKey getCreateColumnNoDefinePrimaryKey() {
        return createColumnNoDefinePrimaryKey;
    }

    public ModifyDataEnforceWhere getModifyDataEnforceWhere() {
        return modifyDataEnforceWhere;
    }

    public PatternRule getCreateIndexName() {
        return createIndexName;
    }

    public PatternRule getUniqueConstraintNameRule() {
        return uniqueConstraintNameRule;
    }

    public PatternRule getPrimaryKeyName() {
        return primaryKeyName;
    }

    public PatternRule getPrimaryKeyMustUseTableName() {
        return primaryKeyMustUseTableName;
    }

    public PatternRule getForeignKeyName() {
        return foreignKeyName;
    }

    public PatternRule getForeignKeyTableAndReferencedTableNameRule() {
        return foreignKeyTableAndReferencedTableNameRule;
    }

    public FileNameNoSpaces getFileNameNoSpaces() {
        return fileNameNoSpaces;
    }

    public NullRule getNoPreconditions() {
        return noPreconditions;
    }

    public NotBlankRule getHasComment() {
        return hasComment;
    }

    public HasValuesRule getHasContext() {
        return hasContext;
    }

    public IsolateDDLChanges getIsolateDDLChanges() {
        return isolateDDLChanges;
    }

    public ValidContext getValidContext() {
        return validContext;
    }

    public SeparateDDLContexts getSeparateDdlContexts() {
        return separateDdlContexts;
    }

    public ModifyDataStartsWithWhere getModifyDataStartsWithWhere() {
        return modifyDataStartsWithWhere;
    }

    Rules mixin(Rules toMix) {
        if (toMix == null) {
            return this;
        }
        return new Rules(
                mix(this.noDuplicateIncludes, toMix.noDuplicateIncludes),
                mix(this.schemaName, toMix.schemaName),
                mix(this.tableNameLength, toMix.tableNameLength),
                mix(this.tableName, toMix.tableName),
                mix(this.objectName, toMix.objectName),
                mix(this.objectNameLength, toMix.objectNameLength),
                mix(this.createTableRemarks, toMix.createTableRemarks),
                mix(this.createColumnRemarks, toMix.createColumnRemarks),
                mix(this.createColumnNullableConstraint, toMix.createColumnNullableConstraint),
                mix(this.createColumnNoDefinePrimaryKey, toMix.createColumnNoDefinePrimaryKey),
                mix(this.modifyDataEnforceWhere, toMix.modifyDataEnforceWhere),
                mix(this.createIndexName, toMix.createIndexName),
                mix(this.uniqueConstraintNameRule, toMix.uniqueConstraintNameRule),
                mix(this.primaryKeyName, toMix.primaryKeyName),
                mix(this.primaryKeyMustUseTableName, toMix.primaryKeyMustUseTableName),
                mix(this.foreignKeyName, toMix.foreignKeyName),
                mix(this.foreignKeyTableAndReferencedTableNameRule, toMix.foreignKeyTableAndReferencedTableNameRule),
                mix(this.fileNameNoSpaces, toMix.fileNameNoSpaces),
                mix(this.noPreconditions, toMix.noPreconditions),
                mix(this.hasComment, toMix.hasComment),
                mix(this.hasContext, toMix.hasContext),
                mix(this.isolateDDLChanges, toMix.isolateDDLChanges),
                mix(this.validContext, toMix.validContext),
                mix(this.separateDdlContexts, toMix.separateDdlContexts),
                mix(this.modifyDataStartsWithWhere, toMix.modifyDataStartsWithWhere));
    }

    private RuleConfig mix(Rule def, Rule toMix) {
        final RuleConfig result = ofNullable(toMix).map(Rule::getRuleConfig).orElse(def.getRuleConfig());
        if (result != null && (result.getErrorMessage() == null || result.getErrorMessage().isEmpty())) {
            result.setErrorMessage(def.getRuleConfig().getErrorMessage());
        }
        return result;
    }

}
