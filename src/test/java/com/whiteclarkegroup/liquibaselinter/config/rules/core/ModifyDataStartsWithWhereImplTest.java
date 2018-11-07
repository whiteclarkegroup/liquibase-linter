package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ChangeSetParameterResolver.class)
class ModifyDataStartsWithWhereImplTest {

    @Test
    void shouldNotAllowWhereConditionToStartWithWhereCaseInsensitive(ChangeSet changeSet) {
        UpdateDataChange updateDataChange = new UpdateDataChange();
        updateDataChange.setTableName("TABLE");
        updateDataChange.setChangeSet(changeSet);
        updateDataChange.setWhere("WHERE table = 'X'");

        ModifyDataStartsWithWhereImpl modifyDataStartsWithWhere = new ModifyDataStartsWithWhereImpl();
        assertTrue(modifyDataStartsWithWhere.invalid(updateDataChange));

        updateDataChange.setTableName("TABLE");
        updateDataChange.setChangeSet(changeSet);
        updateDataChange.setWhere("where table = 'X'");
        assertTrue(modifyDataStartsWithWhere.invalid(updateDataChange));
    }

}
