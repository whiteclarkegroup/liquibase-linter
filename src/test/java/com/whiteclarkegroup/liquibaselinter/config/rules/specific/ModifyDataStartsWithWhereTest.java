package com.whiteclarkegroup.liquibaselinter.config.rules.specific;

import liquibase.change.core.AbstractModifyDataChange;
import liquibase.change.core.UpdateDataChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModifyDataStartsWithWhereTest {

    private ModifyDataStartsWithWhere modifyDataStartsWithWhere;

    @BeforeEach
    void setUp() {
        modifyDataStartsWithWhere = new ModifyDataStartsWithWhere(null);
    }

    @DisplayName("null where clause should be valid")
    @Test
    void nullWhereClauseShouldBeValid() {
        assertFalse(modifyDataStartsWithWhere.invalid(withWhere(null), null));
    }

    @DisplayName("where clause not starting with 'where' should be valid")
    @Test
    void nullClauseNotStartingWithWhereShouldBeValid() {
        assertFalse(modifyDataStartsWithWhere.invalid(withWhere("TEST"), null));
    }

    @DisplayName("where clause starting with 'where' should be invalid")
    @Test
    void nullClauseStartingWithWhereShouldBeInvalid() {
        assertTrue(modifyDataStartsWithWhere.invalid(withWhere("wHerE"), null));
    }

    private AbstractModifyDataChange withWhere(String where) {
        UpdateDataChange updateDataChange = new UpdateDataChange();
        updateDataChange.setWhere(where);
        return updateDataChange;

    }
}
