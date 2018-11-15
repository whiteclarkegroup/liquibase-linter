package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.whiteclarkegroup.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.CreateTableChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({ChangeSetParameterResolver.class})
class CreateTableRemarksRuleImplTest {

    private CreateTableRemarksRuleImpl createTableRemarksRule;

    @BeforeEach
    void setUp() {
        createTableRemarksRule = new CreateTableRemarksRuleImpl();
    }

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowCreteTableWithoutRemarks(ChangeSet changeSet) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);

        assertTrue(createTableRemarksRule.invalid(createTableChange));
    }

    @DisplayName("Should allow create table with remarks attribute")
    @Test
    void shouldAllowCreateTableWithRemarks(ChangeSet changeSet) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setRemarks("REMARK");
        createTableChange.setChangeSet(changeSet);
        changeSet.addChange(createTableChange);
        assertFalse(createTableRemarksRule.invalid(createTableChange));
    }

}
