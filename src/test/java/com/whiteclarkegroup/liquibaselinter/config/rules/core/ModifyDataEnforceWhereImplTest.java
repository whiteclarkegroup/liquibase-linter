package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ChangeSetParameterResolver.class)
class ModifyDataEnforceWhereImplTest {

    @Test
    void shouldEnforceWhereConditionOnCertainTables(ChangeSet changeSet) {
        UpdateDataChange updateDataChange = new UpdateDataChange();
        updateDataChange.setTableName("REQUIRES_WHERE");
        updateDataChange.setChangeSet(changeSet);

        final ModifyDataEnforceWhereImpl modifyDataEnforceWhere = new ModifyDataEnforceWhereImpl();
        modifyDataEnforceWhere.configure(RuleConfig.builder().withValues(Collections.singletonList("REQUIRES_WHERE")).build());
        assertTrue(modifyDataEnforceWhere.invalid(updateDataChange));
    }

}
