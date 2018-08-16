package com.wcg.liquibase;

import com.wcg.liquibase.linters.*;
import liquibase.change.core.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LinterFactoryTest {

    @DisplayName("Should return add column linter for add column change type")
    @Test
    void should_return_add_column_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new AddColumnChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), AddColumnChangeLinter.class)
        );
    }

    @DisplayName("Should return create table linter for create table change type")
    @Test
    void should_return_create_table_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new CreateTableChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), CreateTableChangeLinter.class)
        );
    }

    @DisplayName("Should return update data change linter")
    @Test
    void should_return_update_data_change_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new UpdateDataChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), ModifyDataChangeLinter.class)
        );
    }

    @DisplayName("Should return delete data change linter")
    @Test
    void should_return_delete_data_change_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new DeleteDataChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), ModifyDataChangeLinter.class)
        );
    }

    @DisplayName("Should return create index change linter")
    @Test
    void should_return_create_index_change_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new CreateIndexChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), CreateIndexChangeLinter.class)
        );
    }

    @DisplayName("Should return add unique constraint linter")
    @Test
    void should_return_add_unique_constraint_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new AddUniqueConstraintChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), AddUniqueConstraintChangeLinter.class)
        );
    }

    @DisplayName("Should return add primary key constraint linter")
    @Test
    void should_return_add_primary_key_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new AddPrimaryKeyChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), AddPrimaryKeyChangeLinter.class)
        );
    }

    @DisplayName("Should return add foreign key constraint linter")
    @Test
    void should_return_add_foreign_key_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new AddForeignKeyConstraintChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), AddForeignKeyConstraintChangeLinter.class)
        );
    }

    @DisplayName("Should return rename table change linter")
    @Test
    void should_return_rename_table_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new RenameTableChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), RenameTableChangeLinter.class)
        );
    }

    @DisplayName("Should return rename view change linter")
    @Test
    void should_return_rename_view_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new RenameViewChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), RenameViewChangeLinter.class)
        );
    }

    @DisplayName("Should return rename column change linter")
    @Test
    void should_return_rename_column_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new RenameColumnChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), RenameColumnChangeLinter.class)
        );
    }

    @DisplayName("Should return merge column change linter")
    @Test
    void should_return_merge_column_linter() {
        final Optional<Linter> linter = LinterFactory.getLinter(new MergeColumnChange());
        assertAll(
                () -> assertTrue(linter.isPresent()),
                () -> assertEquals(linter.get().getClass(), MergeColumnChangeLinter.class)
        );
    }
}
