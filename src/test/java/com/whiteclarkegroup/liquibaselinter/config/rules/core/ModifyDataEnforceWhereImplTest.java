package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.DeleteDataChange;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ChangeSetParameterResolver.class)
class ModifyDataEnforceWhereImplTest {

    @Test
    void shouldEnforceWhereConditionOnCertainTablesNullValue(ChangeSet changeSet) {
        final ModifyDataEnforceWhereImpl modifyDataEnforceWhere = new ModifyDataEnforceWhereImpl();
        modifyDataEnforceWhere.configure(RuleConfig.builder().withValues(Collections.singletonList("REQUIRES_WHERE")).build());
        assertTrue(modifyDataEnforceWhere.invalid(getUpdateDataChange(changeSet, null)));
        assertTrue(modifyDataEnforceWhere.invalid(getDeleteDataChange(changeSet, null)));
    }

    @Test
    void shouldEnforceWhereConditionOnCertainTablesEmptyValue(ChangeSet changeSet) {
        final ModifyDataEnforceWhereImpl modifyDataEnforceWhere = new ModifyDataEnforceWhereImpl();
        modifyDataEnforceWhere.configure(RuleConfig.builder().withValues(Collections.singletonList("REQUIRES_WHERE")).build());
        assertTrue(modifyDataEnforceWhere.invalid(getUpdateDataChange(changeSet, "")));
        assertTrue(modifyDataEnforceWhere.invalid(getDeleteDataChange(changeSet, "")));
    }

    @Test
    void shouldEnforcePresenceAndPattern(ChangeSet changeSet) {
        ModifyDataEnforceWhereImpl rule = new ModifyDataEnforceWhereImpl();
        rule.configure(RuleConfig.builder()
            .withValues(Collections.singletonList("REQUIRES_WHERE"))
            .withPattern("^.*CODE =.*$")
            .build());

        assertTrue(rule.invalid(getUpdateDataChange(changeSet, null)));
        assertTrue(rule.invalid(getUpdateDataChange(changeSet, "sausages")));
        assertFalse(rule.invalid(getUpdateDataChange(changeSet, "CODE = 'foo'")));
    }

    @DisplayName("Modify data change should support formatter error messages")
    @Test
    void foreignKeyNameRuleShouldReturnFormattedErrorMessage(ChangeSet changeSet) {
        ModifyDataEnforceWhereImpl rule = new ModifyDataEnforceWhereImpl();
        rule.configure(RuleConfig.builder().build());
        assertEquals(rule.getMessage(getUpdateDataChange(changeSet, null)), "Modify data on table 'REQUIRES_WHERE' must have a where condition");
        assertEquals(rule.getMessage(getDeleteDataChange(changeSet, null)), "Modify data on table 'REQUIRES_WHERE' must have a where condition");
    }

    private UpdateDataChange getUpdateDataChange(ChangeSet changeSet, String where) {
        UpdateDataChange updateDataChange = new UpdateDataChange();
        updateDataChange.setTableName("REQUIRES_WHERE");
        updateDataChange.setWhere(where);
        updateDataChange.setChangeSet(changeSet);
        return updateDataChange;
    }

    private DeleteDataChange getDeleteDataChange(ChangeSet changeSet, String where) {
        DeleteDataChange deleteDataChange = new DeleteDataChange();
        deleteDataChange.setTableName("REQUIRES_WHERE");
        deleteDataChange.setWhere(where);
        deleteDataChange.setChangeSet(changeSet);
        return deleteDataChange;
    }

}
